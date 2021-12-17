package edu.puc.core2.parser.plan.query;

public enum ConsumptionPolicy {
    ANY,
    PARTITION,
    NONE;

    public static ConsumptionPolicy getDefault() {
        return ANY;
    }
}
