package com.example.exp_final_alpha.util;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleCountingIdlingResource implements IdlingResource{
    private final String mResourceName;

    private final AtomicInteger counter = new AtomicInteger(0);

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String resourceName) {
        mResourceName=resourceName;
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get()==0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback=callback;
    }

    public void increment(){
        counter.getAndIncrement();
    }

    public void decrement(){
        int counterVal=counter.decrementAndGet();
        if(counterVal==0){
            if(null!=resourceCallback){
                resourceCallback.onTransitionToIdle();
            }
        }
        if (counterVal<0){
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}
