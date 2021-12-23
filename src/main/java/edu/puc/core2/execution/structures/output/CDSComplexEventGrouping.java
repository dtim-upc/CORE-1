package edu.puc.core2.execution.structures.output;

import edu.puc.core2.runtime.events.Event;
import edu.puc.core2.util.DistributionConfiguration;
import org.antlr.v4.runtime.misc.Pair;

import java.util.Optional;


/**
 * This is basically an iterator from final state nodes.
 * The iterator will give you a list of Complex Events using the enumeration Algorithm 2 [CORE paper].
 */
public abstract class CDSComplexEventGrouping<T> implements Iterable<ComplexEvent> {
    public abstract long size();
    public abstract Event getLastEvent();
    public abstract void addCDSNode(T rootNode);
    protected abstract Optional<DistributionConfiguration> getDistributionConfiguration();


    // This method is used by ranged enumeration (aka distributed enumeration).
    protected Pair<Integer, Integer> getEnumerationParameters(int paths) {
        DistributionConfiguration tmp = getDistributionConfiguration().orElse(DistributionConfiguration.DEFAULT);
        /*
        paths = 18
        processes = 4
            q = 4
            r = 2
            process = 0
              a = 5
              s = 0
            process = 1
              a = 5
              s = 5
            process = 2
              a = 4
              s = 10
            process = 3
              a = 4
              s = 14
        */
        int q = (int) Math.floor((double) paths / (double) tmp.processes);
        int r = paths - q*tmp.processes;
        int a = q;
        if (tmp.process < r) {
            a += 1;
        }
        int s = tmp.process*q + Math.min(tmp.process, r);
        return new Pair<>(a, s);
    }
}