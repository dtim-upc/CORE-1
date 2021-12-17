package edu.puc.core2.execution.structures.output;

import edu.puc.core2.parser.plan.cea.Transition;
import edu.puc.core2.runtime.events.Event;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ComplexEvent implements Iterable<Event> {
    private ComplexEventNode head;
    private long end = -1;
    private long start = -1;

    ComplexEventNode push(Event event, Transition.TransitionType transitionType) {
        start = event.getIndex();

        if (end == -1) {
            end = event.getIndex();
        }

        if (head != null){
            head = new ComplexEventNode(event, head, transitionType);
        }
        else {
            head = new ComplexEventNode(event, transitionType);
        }
        return head;
    }

    // This is performant implementation of popUntil.
    void popUntil(ComplexEventNode node) {
        head = node;
    }

    public Iterator<Event> iterator(){
        return new Iterator<Event>() {
            ComplexEventNode current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Event next() {
                Event event = current.getEvent();
                current = current.getNext();
                return event;
            }
        };
    }

    public long size(){
        return head != null ? head.getIndex() : 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long newStart) {
        start = newStart;
    }

    public long getEnd() {
        return end;
    }

    public ComplexEventNode getHead() {
        return head;
    }


    public static List<Long> complexEvent2Indexes(ComplexEvent ce) {
        return StreamSupport.stream(ce.spliterator(), true).map(Event::getIndex).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return complexEvent2Indexes(this).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        List<Long> events = complexEvent2Indexes(this);
        List<Long> otherEvents = complexEvent2Indexes((ComplexEvent) o);

        return events.equals(otherEvents);
    }

    @Override
    public int hashCode() {
        return complexEvent2Indexes(this).hashCode();
    }
}
