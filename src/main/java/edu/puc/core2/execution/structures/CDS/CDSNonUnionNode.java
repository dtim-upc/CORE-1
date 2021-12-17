package edu.puc.core2.execution.structures.CDS;

public abstract class CDSNonUnionNode extends CDSNode {
    public static final CDSNonUnionNode BOTTOM = new CDSNonUnionNode() {
        @Override
        public int getPaths() {
            return 1;
        }

        @Override
        public boolean isBottom() {
            return true;
        }

        @Override
        public String toString() {
            return "Bottom";
        }
    };
}
