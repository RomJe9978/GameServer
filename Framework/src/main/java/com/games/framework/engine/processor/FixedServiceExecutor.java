package com.games.framework.engine.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author liuxuanjie
 */
public class FixedServiceExecutor implements ITaskExecutor<EngineTask> {

    private ExecutorService[] serviceRunner;


    private FixedServiceExecutor(int threadCount) {
        serviceRunner = new ExecutorService[threadCount];
        for (int i = 0; i < threadCount; i++) {
            serviceRunner[i] = Executors.newSingleThreadExecutor();
        }
    }

    public static FixedServiceExecutor newInstance(int threadCount) {
        return new FixedServiceExecutor(threadCount);
    }

    @Override
    public boolean canSubmit() {
        return true;
    }

    @Override
    public Future<?> submit(EngineTask task) {
        long serviceID = task.getServerService().getServiceId();
        return serviceRunner[(int) (serviceID % serviceRunner.length)].submit(task);
    }

    @Override
    public void handleFuture(Future<?> future) {
    }
}
