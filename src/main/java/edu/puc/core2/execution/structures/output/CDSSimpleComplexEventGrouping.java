package edu.puc.core2.execution.structures.output;


import edu.puc.core2.execution.structures.CDS.CDSNode;
import edu.puc.core2.execution.structures.CDS.CDSNonUnionNode;
import edu.puc.core2.execution.structures.CDS.CDSOutputNode;
import edu.puc.core2.execution.structures.CDS.CDSUnionNode;
import edu.puc.core2.runtime.events.Event;
import edu.puc.core2.util.DistributionConfiguration;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;

import java.util.*;

/** Equivalent to {@link CDSTimeComplexEventGrouping} without time-windows; hence more performant. */
public class CDSSimpleComplexEventGrouping extends CDSComplexEventGrouping<CDSNode> {
    final private long totalMatches;
    final private Event lastEvent;
    final private long limit;
    final private List<CDSNode> CDSNodes;
    final private Optional<DistributionConfiguration> distributionConfiguration;

    public CDSSimpleComplexEventGrouping(Event currentEvent, long limit, Optional<DistributionConfiguration> distributionConfiguration){
        this.lastEvent = currentEvent;
        this.limit = limit;
        this.distributionConfiguration = distributionConfiguration;
        this.totalMatches = 0;
        this.CDSNodes = new ArrayList<>();
    }

    @Override
    public void addCDSNode(CDSNode rootNode) {
        CDSNodes.add(rootNode);
    }

    @Override
    public long size(){
        return totalMatches;
    }

    @Override
    public Event getLastEvent(){
        return lastEvent;
    }

    @Override
    protected Optional<DistributionConfiguration> getDistributionConfiguration() {
        return this.distributionConfiguration;
    }

    // 1) This iterator will modify the complex events after each iteration.
    //    Be careful, if you try to collect them in a stream, each CE will change as soon as you call next().
    //    In order to avoid this, you need to do a lot of copies so we decided not to do that change.
    // 2) The distributed enumeration may return 'null' between different CDS.
    //    Just skip the null and the next 'next()' will return a proper complex event if there are remaining CDS.
    public Iterator<ComplexEvent> iterator(){
        return new Iterator<>() {
            final Iterator<CDSNode> CDSNodeIterator = CDSNodes.iterator();
            CDSNode current = CDSNodeIterator.next();
            final Stack<Triple<CDSNode, ComplexEventNode, Pair<Integer, Integer>>> stack = new Stack<>();
            ComplexEvent complexEvent = new ComplexEvent();
            final Pair<Integer, Integer> enumerationParameters = getEnumerationParameters(current.getPaths());
            int a1 = enumerationParameters.a;
            int s1 = enumerationParameters.b;

            @Override
            public boolean hasNext() {
                if (!current.isBottom()) {
                    return true;
                }
                if (!stack.isEmpty()) {
                    return true;
                }
                return CDSNodeIterator.hasNext();
            }

            @Override
            public ComplexEvent next() {
                if (current.isBottom() && stack.isEmpty()) {
                    complexEvent = new ComplexEvent();
                    current = CDSNodeIterator.next();
                    final Pair<Integer, Integer> enumerationParameters = getEnumerationParameters(current.getPaths());
                    a1 = enumerationParameters.a;
                    s1 = enumerationParameters.b;
                }
                // Edge case that only happens when the complex event has no union nodes.
                // This prevents any traversal if the process doesn't have any work left
                if (current.getPaths() <= s1) {
                    current = CDSNonUnionNode.BOTTOM;
                    return null;
                }
                while (true) {
                    if (current.isBottom()) {
                        if (stack.isEmpty()) {
                            return  null;
                        }
                        Triple<CDSNode, ComplexEventNode, Pair<Integer, Integer>> triplet = stack.pop();
                        current = triplet.a;
                        complexEvent.popUntil(triplet.b);
                        s1 = triplet.c.a;
                        a1 = triplet.c.b;
                    } else if (current instanceof CDSOutputNode) {
                        CDSOutputNode temp = (CDSOutputNode) current;
                        if (temp.getTransitionType().isBlack()) {
                            complexEvent.push(temp.getEvent(), temp.getTransitionType());
                        } else {
                            Event toAdd = new Event(temp.getEvent().getIndex());
                            complexEvent.push(toAdd, temp.getTransitionType());
                        }
                        current = temp.getChild();
                        if (current.isBottom()) {
                            return complexEvent;
                        }
                    } else if (current instanceof CDSUnionNode) {
                        CDSUnionNode temp = (CDSUnionNode) current;
                        CDSNode left = temp.getLeft();
                        CDSNode right = temp.getRight();

                        // Right branch
                        {
                            int a2 = (left.getPaths() > s1) ? (a1 - Math.max(0, left.getPaths() - s1)) : a1;
                            int s2 = Math.max(0, s1 - left.getPaths());
                            if (right.getPaths() > s2 && a2 > 0) {
                                stack.push(new Triple<>(right, complexEvent.getHead(), new Pair<>(s2, a2)));
                            }
                        }

                        // Left branch
                        if (left.getPaths() > s1) {
                            current = left;
                        } else {
                            // Go to the next node in the stack or CDSNodeIterator
                            current = CDSNonUnionNode.BOTTOM;
                        }
                    }
                }
            }
        };
    }
}