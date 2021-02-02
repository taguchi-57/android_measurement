package com.example.android.hrs.blueprints.jumpmeasurementapp.util;


import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * An simple counter implementation of {@link IdlingResource} that determines idleness by
 * maintaining an internal counter. When the counter is 0 - it is considered to be idle, when it is
 * non-zero it is not idle. This is very similar to the way a {@link java.util.concurrent.Semaphore}
 * behaves.
 * 内部カウンターを維持することによってアイドル状態を判別する{@linkIdlingResource}の単純なカウンター実装。
 * カウンターが0の場合、アイドル状態と見なされ、ゼロ以外の場合、アイドル状態ではありません。
 * これは、{@link java.util.concurrent.Semaphore}の動作と非常によく似ています。
 * <p>
 * This class can then be used to wrap up operations that while in progress should block tests from
 * accessing the UI.
 * 次に、このクラスを使用して、
 * 進行中にテストがUIにアクセスするのをブロックする必要がある操作をまとめることができます。
 */
public final class SimpleCountingIdlingResource implements IdlingResource{

    private final String mResourceName;

    private final AtomicInteger counter = new AtomicInteger(0);

    // written from main thread, read from any thread.
    private volatile IdlingResource.ResourceCallback resourceCallback;

    /**
     * Creates a SimpleCountingIdlingResource
     *
     * @param resourceName the resource name this resource should report to Espresso.
     */
    public SimpleCountingIdlingResource(String resourceName) {
        mResourceName = checkNotNull(resourceName);
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    /**
     * Increments the count of in-flight transactions to the resource being monitored.
     */
    public void increment() {
        counter.getAndIncrement();
    }

    /**
     * Decrements the count of in-flight transactions to the resource being monitored.
     *
     * If this operation results in the counter falling below 0 - an exception is raised.
     *
     * @throws IllegalStateException if the counter is below 0.
     */
    public void decrement() {
        int counterVal = counter.decrementAndGet();
        if (counterVal == 0) {
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
        }

        if (counterVal < 0) {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}
