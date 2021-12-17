package edu.puc.core2.execution.structures.output;

import edu.puc.core2.parser.plan.cea.Transition;
import edu.puc.core2.runtime.events.Event;

class ComplexEventNode {
    private final Event event;
    private ComplexEventNode next;
    private final Transition.TransitionType transitionType;
    private final long index;

    public ComplexEventNode(Event event, ComplexEventNode next, Transition.TransitionType transitionType) {
        this.event = event;
        this.next = next;
        this.transitionType = transitionType;
        this.index = next.getIndex() + 1;
    }


    public ComplexEventNode(Event event, Transition.TransitionType transitionType){
        this.event = event;
        this.transitionType = transitionType;
        this.index = 1;
    }

    public Event getEvent() {
        return event;
    }

    public ComplexEventNode getNext() {
        return next;
    }

    public long getIndex() {
        return index;
    }
}
