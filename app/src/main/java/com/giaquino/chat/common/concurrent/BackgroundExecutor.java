package com.giaquino.chat.common.concurrent;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gian Darren Azriel Aquino.
 */
public class BackgroundExecutor implements Executor {

    private final ExecutorService executorService;

    public BackgroundExecutor() {
        executorService = Executors.newCachedThreadPool(runnable -> new Thread() {
            @Override public void run() {
                setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                runnable.run();
            }
        });
    }

    @WorkerThread @Override public void execute(@NonNull Runnable command) {
        executorService.execute(command);
    }
}
