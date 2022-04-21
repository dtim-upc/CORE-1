package edu.puc.core2.execution.structures.CDS.time;

import edu.puc.core2.parser.plan.cea.Transition;
import edu.puc.core2.runtime.events.Event;

import java.util.HashMap;

public class CDSTimeOutputNode extends CDSTimeNode {
    final private Event event;
    final private Transition.TransitionType transitionType;
    final private CDSTimeNode child;
    private final HashMap<Long, Integer> paths;
    final private long max;

    /** Use {@link CDSNodeManager} to create CDSTimeNodes. */
    CDSTimeOutputNode(CDSTimeNode child, Transition.TransitionType transitionType, Event event, long currentTime) {
        this.child = child;
        this.transitionType = transitionType;
        this.event = event;

        // This is a strong reference to the child's HashMap.
        // If the child is prune, the HashMap will not be freed.
        this.paths = child.getPaths();

        // BOTTOM is no longer used to mark the bottom node.
//        if (child == CDSTimeBottomNode.BOTTOM) {
//            this.max = currentTime;
//        } else {
//            this.max = child.getMax();
//        }
        this.max = child.getMax();
    }

    public Event getEvent() {
        return event;
    }

    public Transition.TransitionType getTransitionType() {
        return transitionType;
    }

    public CDSTimeNode getChild() {
        return this.child;
    }

    @Override
    public boolean isBottom() {
        return false;
    }

    @Override
    public long getMax() {
        return this.max;
    }

    @Override
    protected HashMap<Long, Integer> getPaths() {
        return this.paths;
    }

    @Override
    public int getPathsCount(long currentTime, long windowDelta) {
        int count = 0;
        long threshold = currentTime - windowDelta;
        for (Long key : this.paths.keySet()) {
            if (key > threshold) {
                count += this.paths.get(key);
            }
        }
        return count;
    }
}
