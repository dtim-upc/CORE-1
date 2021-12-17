package edu.puc.core2.runtime.predicates;


import edu.puc.core2.parser.plan.predicate.AndPredicate;
import edu.puc.core2.runtime.events.Event;

import java.util.Collection;
import java.util.stream.Collectors;

public class AndEvaluator extends PredicateEvaluator {

    private final Collection<PredicateEvaluator> predicateEvaluators;

    public AndEvaluator(Collection<PredicateEvaluator> predicateEvaluators){
        this.predicateEvaluators = predicateEvaluators;
    }

    public AndEvaluator(AndPredicate predicate){
        predicateEvaluators = predicate.getPredicates().stream()
                .map(PredicateEvaluator::getEvaluatorForPredicate)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eval(Event event) {
        return predicateEvaluators.stream().allMatch(evaluator -> evaluator.eval(event));
    }
}
