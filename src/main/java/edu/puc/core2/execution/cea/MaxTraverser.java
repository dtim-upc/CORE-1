package edu.puc.core2.execution.cea;

import edu.puc.core2.execution.structures.states.DoubleStateSet;
import edu.puc.core2.execution.structures.states.State;
import edu.puc.core2.execution.structures.states.StateTuple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaxTraverser extends Traverser<DoubleStateSet> {

    private static HashMap<ExecutableCEA, MaxTraverser> traverserMap = new HashMap<>();

    private MaxTraverser(ExecutableCEA cea) {
        super(cea);
    }

    @Override
    StateTuple calculateNextState(State<DoubleStateSet> fromState, BitSet vector) {
        // Same algorithm as in section 7.2 (max case)
        // The final state is not modified though..
        StateTuple ret = new StateTuple();

        Set<Integer> blackSetR = new HashSet<>();
        Set<Integer> blackSetS = new HashSet<>();

        Set<Integer> whiteSetR = new HashSet<>();
        Set<Integer> whiteSetS = new HashSet<>();

        /* R set */
        getNextStates(fromState.getStateSet().getFirstStateSet(), vector, blackSetR, whiteSetR);

        /* S set (W set in the paper) */
        getNextStates(fromState.getStateSet().getSecondStateSet(), vector, blackSetS, whiteSetS);

        /* Black State */
        Set<Integer> newBlackR = new HashSet<>(blackSetR);
        newBlackR.removeAll(blackSetS);
        ret.setBlackState(getStateFromStateSet(new DoubleStateSet(newBlackR, blackSetS)));

        /* White State */
        Set<Integer> newWhiteR = new HashSet<>(whiteSetR);
        Set<Integer> newWhiteS = new HashSet<>(whiteSetS);
        newWhiteS.addAll(blackSetS);
        newWhiteS.addAll(blackSetR);
        newWhiteR.removeAll(newWhiteS);
        ret.setWhiteState(getStateFromStateSet(new DoubleStateSet(newWhiteR, newWhiteS)));

        knownTransitionsList.get(fromState.getId()).put(vector, ret);
        return ret;
    }

    /**
     * Get the {@link MaxTraverser} associated to the given CEA.
     * @param cea {@link ExecutableCEA} to get the traverser for.
     * @return Previously (or newly) created {@link MaxTraverser}.
     */
    public static MaxTraverser getInstance(ExecutableCEA cea) {
        MaxTraverser instance = traverserMap.get(cea);
        if (instance == null) {
            instance = new MaxTraverser(cea);
            instance.init(new DoubleStateSet(
                    Stream.of(instance.INITIAL).collect(Collectors.toSet()),
                    Collections.emptySet()
            ));
            traverserMap.put(cea, instance);
        }
        return instance;
    }
}

