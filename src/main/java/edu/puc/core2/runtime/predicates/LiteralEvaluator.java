package edu.puc.core2.runtime.predicates;

import edu.puc.core2.parser.plan.values.Literal;
import edu.puc.core2.runtime.events.Event;

public class LiteralEvaluator extends ValueEvaluator {

    private final Object value;

    public LiteralEvaluator(Literal literal){
        value = literal.getValue();
    }

    @Override
    public Object eval(Event event){
        return value;
    }
}
