package edu.puc.core2.runtime.predicates;


import edu.puc.core2.parser.plan.predicate.OrPredicate;
import edu.puc.core2.runtime.events.Event;

import java.util.Collection;
import java.util.stream.Collectors;

public class OrEvaluator extends PredicateEvaluator {

    private final Collection<PredicateEvaluator> predicateEvaluators;

    public OrEvaluator(OrPredicate predicate){
        predicateEvaluators = predicate.getPredicates().stream()
                .map(PredicateEvaluator::getEvaluatorForPredicate)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eval(Event event) {
        return predicateEvaluators.stream().anyMatch(evaluator -> evaluator.eval(event));
    }
}
