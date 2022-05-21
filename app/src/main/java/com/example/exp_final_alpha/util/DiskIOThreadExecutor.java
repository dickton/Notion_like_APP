package com.example.exp_final_alpha.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOThreadExecutor implements Executor {
    private final Executor mDiskIO;
    public DiskIOThreadExecutor(){
        mDiskIO= Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable command) {
        mDiskIO.execute(command);
    }
}
