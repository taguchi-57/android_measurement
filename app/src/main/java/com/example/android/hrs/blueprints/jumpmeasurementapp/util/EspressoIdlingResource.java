package com.example.android.hrs.blueprints.jumpmeasurementapp.util;


import androidx.test.espresso.IdlingResource;

/**
 * Contains a static reference to {@link IdlingResource}, only available in the 'mock' build type.
 * {@linkIdlingResource}への静的参照が含まれています。これは「モック」ビルドタイプでのみ使用できます。
 */
/*
* ↑意味不明
* */
public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }

}



