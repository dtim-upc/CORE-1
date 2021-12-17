package edu.puc.core2.parser.plan.query;

public enum SelectionStrategy {
    ALL,
    MAX,
    NEXT,
    LAST,
    STRICT;

    public static SelectionStrategy getDefault() {
        return ALL;
    }
}
