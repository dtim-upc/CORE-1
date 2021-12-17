package edu.puc.core.execution.structures.CDS;

public abstract class CDSNode {
    /** The number of paths from this node */
    abstract public int getPaths();
    abstract public boolean isBottom();
}
