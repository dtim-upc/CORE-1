package edu.puc.core2.runtime.predicates;


import edu.puc.core2.parser.plan.predicate.LikePredicate;
import edu.puc.core2.runtime.events.Event;

public class LikeEvaluator  extends PredicateEvaluator {

    public LikeEvaluator(LikePredicate predicate){

    }

    @Override
    public boolean eval(Event event) {
        return false;
    }
}
