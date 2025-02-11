package com.al3x.housing2.Utils;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AsyncTask {
    UUID id;
    //1000ms
    Consumer<AsyncTask> runnable;

    int millis = 0;

    public AsyncTask(Consumer< AsyncTask> runnable) {
        this.id = UUID.randomUUID();
        this.runnable = runnable;
    }

    public UUID getId() {
        return id;
    }

    public Consumer<AsyncTask> getRunnable() {
        return runnable;
    }

    public void reset() {
        millis = 0;
    }

    public void run() {
        if (runnable == null) {
            return;
        }
        runnable.accept(this);
    }

    public void cancel() {
        runnable = null;
    }

    public boolean isCancelled() {
        return runnable == null;
    }
}
