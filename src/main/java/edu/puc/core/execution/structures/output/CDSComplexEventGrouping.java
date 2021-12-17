package edu.puc.core.execution.structures.output;

import edu.puc.core.execution.structures.CDS.CDSNode;
import edu.puc.core.runtime.events.Event;
import edu.puc.core.util.DistributionConfiguration;
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
        int a = (int) Math.ceil((double) paths / (double) tmp.processes);
        int s = a * tmp.process;
        return new Pair<>(a, s);
    }
}