package edu.puc.core2.parser.plan.filter;

import edu.puc.core2.parser.plan.cea.CEA;
import edu.puc.core2.parser.plan.cea.OrCEA;

public class OrFilter extends Filter {
    private Filter left;
    private Filter right;

    public OrFilter(Filter left, Filter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        CEA leftCea = left.applyToCEA(cea.copy());
        CEA rightCea = right.applyToCEA(cea.copy());
        return new OrCEA(leftCea, rightCea);
    }
}
