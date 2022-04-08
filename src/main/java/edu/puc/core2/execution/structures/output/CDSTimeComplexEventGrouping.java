package edu.puc.core2.execution.structures.output;

import edu.puc.core2.execution.structures.CDS.time.CDSTimeBottomNode;
import edu.puc.core2.execution.structures.CDS.time.CDSTimeNode;
import edu.puc.core2.execution.structures.CDS.time.CDSTimeOutputNode;
import edu.puc.core2.execution.structures.CDS.time.CDSTimeUnionNode;
import edu.puc.core2.runtime.events.Event;
import edu.puc.core2.util.DistributionConfiguration;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;

import java.util.*;

public class CDSTimeComplexEventGrouping extends CDSComplexEventGrouping<CDSTimeNode> {
    final private long totalMatches;
    final private Event lastEvent;
    final private long limit;
    final private long windowDelta;
    final private List<CDSTimeNode> CDSTimeNodes;
    final private long currentTime;
    final private Optional<DistributionConfiguration> distributionConfiguration;

    public CDSTimeComplexEventGrouping(Event currentEvent, long limit, long windowDelta, long currentTime, Optional<DistributionConfiguration> distributionConfiguration){
        super();
        this.lastEvent = currentEvent;
        this.limit = limit;
        this.distributionConfiguration = distributionConfiguration;
        this.windowDelta = windowDelta;
        this.currentTime = currentTime;
        this.totalMatches = 0;
        this.CDSTimeNodes = new ArrayList<>();
    }

    @Override
    public void addCDSNode(CDSTimeNode rootNode) {
        this.CDSTimeNodes.add(rootNode);
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

    public Iterator<ComplexEvent> iterator(){
        return new Iterator<>() {
            final Iterator<CDSTimeNode> CDSNodeIterator = CDSTimeNodes.iterator();
            CDSTimeNode current = CDSNodeIterator.next();
            final Stack<Triple<CDSTimeNode, ComplexEventNode, Pair<Integer, Integer>>> stack = new Stack<>();
            ComplexEvent complexEvent = new ComplexEvent();
            final Pair<Integer, Integer> enumerationParameters = getEnumerationParameters(current.getPathsCount(currentTime, windowDelta));
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
                    final Pair<Integer, Integer> enumerationParameters = getEnumerationParameters(current.getPathsCount(currentTime, windowDelta));
                    a1 = enumerationParameters.a;
                    s1 = enumerationParameters.b;
                }
                // Prune beforehand.
                if (currentTime - current.getMax() > windowDelta) {
                    current = CDSTimeBottomNode.BOTTOM;
                    return null;
                }
                // Edge case that only happens when the complex event has no union nodes.
                // This prevents any traversal if the process doesn't have any work left
                if (current.getPathsCount(currentTime, windowDelta) <= s1) {
                    current = CDSTimeBottomNode.BOTTOM;
                    return null;
                }
                while (true) {
                    if (current == null || current.isBottom()) {
                        if (stack.isEmpty()) {
                            return null;
                        }
                        Triple<CDSTimeNode, ComplexEventNode, Pair<Integer, Integer>> triplet = stack.pop();
                        current = triplet.a;
                        complexEvent.popUntil(triplet.b);
                        s1 = triplet.c.a;
                        a1 = triplet.c.b;
                    } else if (current instanceof CDSTimeOutputNode) {
                        CDSTimeOutputNode temp = (CDSTimeOutputNode) current;
                        if (temp.getTransitionType().isBlack()) {
                            complexEvent.push(temp.getEvent(), temp.getTransitionType());
                        } else {
                            Event toAdd = new Event(temp.getEvent().getIndex());
                            complexEvent.push(toAdd, temp.getTransitionType());
                        }
                        current = temp.getChild();
                        // TODO: can this ever happen
                        if (current == null) {
                            // This will happen when the node has been pruned and garbage collected.
                            current = CDSTimeBottomNode.BOTTOM;
                        }
                        // TODO: is this check needed?
                        else if (currentTime - current.getMax() > windowDelta) {
                            // Prune the node if necessary.
                            current = CDSTimeBottomNode.BOTTOM;
                        } else if (current.isBottom()) {
                            // Invariant: currentTime - temp.getMax() <= windowDelta
                            complexEvent.setStart(current.getMax());
                            return complexEvent;
                        }
                    } else if (current instanceof CDSTimeUnionNode) {
                        CDSTimeUnionNode u = (CDSTimeUnionNode) current;
                        CDSTimeNode left = u.getLeft();
                        CDSTimeNode right = u.getRight();

                        // Right branch
                        {
                            if (right.getMax() >= currentTime - windowDelta) {
                                int a2 = a1 - Math.max(0, left.getPathsCount(currentTime, windowDelta) - s1);
                                if (a2 > 0) {
                                    // If you remove the max, the calculus of a2 will be wrong since it is subtracting s1.
                                    int s2 = Math.max(0, s1 - left.getPathsCount(currentTime, windowDelta));
                                    stack.push(new Triple<>(right, complexEvent.getHead(), new Pair<>(s2, a2)));
                                }
                            }
                        }

                        // Left branch
                        // There is no need to look at the time-window since on the next iteration
                        // there will be a non-union node that will check the time-window.
                        if (left.getPathsCount(currentTime, windowDelta) > s1) {
                            current = left;
                        } else {
                            // Go to the next node in the stack or CDSNodeIterator
                            current = CDSTimeBottomNode.BOTTOM;
                        }
                    }
                }
            }
        };
    }
}
