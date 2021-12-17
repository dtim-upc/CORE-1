package edu.puc.core2.execution.structures.states;

import java.util.HashSet;
import java.util.Set;

/** Set of integers representing states */
public class SimpleStateSet {

    protected final Set<Integer> stateSet;

    public SimpleStateSet(Set<Integer> stateSet) {
        this.stateSet = new HashSet<>(stateSet);
    }

    public Set<Integer> getStateSet() {
        return stateSet;
    }

    @Override
    public int hashCode(){
        return stateSet.hashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if (this == obj) return true;
        if (!(obj instanceof SimpleStateSet)) return false;
        return stateSet.equals(((SimpleStateSet)obj).stateSet);
    }

    public boolean isEmpty(){
        return stateSet.isEmpty();
    }

    @Override
    public String toString(){
        return "SimpleStateSet(" + stateSet + ")";
    }

}