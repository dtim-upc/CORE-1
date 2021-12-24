package edu.puc.core2.engine;

import edu.puc.core2.engine.executors.ExecutorManager;
import edu.puc.core2.engine.streams.StreamManager;
import edu.puc.core2.execution.BaseExecutor;
import edu.puc.core2.execution.structures.output.CDSComplexEventGrouping;
import edu.puc.core2.runtime.events.Event;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;


public class Engine extends BaseEngine {

    public Engine(ExecutorManager executorManager, StreamManager streamManager) {
        this.executorManager = executorManager;
        this.streamManager = streamManager;
        // Use the default callback
    }

    public Engine(ExecutorManager executorManager, StreamManager streamManager, Consumer<CDSComplexEventGrouping> matchCallback) {
        this(executorManager, streamManager);
        setMatchCallback(matchCallback);
    }

    @Override
    public void sendEvent(Event e) {
        for (BaseExecutor executor : getExecutors().values()) {
            executor.sendEvent(e);
        }
    }

    @Override
    public void setMatchCallback(Consumer<CDSComplexEventGrouping> callback) {
        executorManager.setDefaultMatchCallback(callback);
    }

    @Override
    public void start(boolean andWait) throws InterruptedException {
        executorManager.start();
        streamManager.start();
        if (andWait) {
            waitExecutorManager();
            while (! streamManager.isReady()) {
                Thread.sleep(100);
            }
        }
    }

    /*
     The following will fail:
         engine.start()
         e = engine.nextEvent()
         engine.next_sendEvent(e) // BOGUS
      The problem is .start() is asynchronous and executors are added on a secondary thread.
      You need to wait until the executor is added in order to send an Event.
    */
    private void waitExecutorManager() throws InterruptedException {
        Map<String, BaseExecutor> executors = getExecutors();
        while (executors.size() == 0) {
           Thread.sleep(100);
           executors = getExecutors();
        }
    }

    @Override
    public Event nextEvent() throws InterruptedException {
        return streamManager.nextEvent();
    }

    @Override
    public Map<String, BaseExecutor> getExecutors() {
        return executorManager.getExecutors();
    }

}
