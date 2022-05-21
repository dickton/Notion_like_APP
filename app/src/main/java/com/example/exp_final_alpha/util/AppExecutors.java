package com.example.exp_final_alpha.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT=2;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    @VisibleForTesting
    AppExecutors(Executor diskIO,Executor networkIO,Executor mainThread){
        this.diskIO=diskIO;
        this.networkIO=networkIO;
        this.mainThread=mainThread;
    }

    public AppExecutors(){
        this(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),new MainThreadExecutor());
    }

    public Executor getDiskIO(){
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
