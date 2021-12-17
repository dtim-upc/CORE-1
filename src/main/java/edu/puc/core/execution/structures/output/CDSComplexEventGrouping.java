package edu.puc.core.execution.structures.output;

import edu.puc.core.execution.structures.CDS.CDSNode;
import edu.puc.core.runtime.events.Event;


/**
 * This is basically an iterator from final state nodes.
 * The iterator will give you a list of Complex Events using the enumeration Algorithm 2 [CORE paper].
 */
public abstract class CDSComplexEventGrouping<T> implements Iterable<ComplexEvent> {
    public abstract long size();
    public abstract Event getLastEvent();
    public abstract void addCDSNode(T rootNode);
}