package com.example.android.hrs.blueprints.jumpmeasurementapp.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Global executor pools for the whole application.
 * アプリケーション全体のグローバルエグゼキュータプール。
 * <p>
 * Grouping tasks like this avoids the effects of media starvation (e.g. disk reads don't wait behind
 * webservice requests).
 * このようにタスクをグループ化すると、メディア不足の影響を回避できます（たとえば、ディスクの読み取りが遅れない
 *    Webサービスリクエスト）。
 */
/*
* ↑意味不明
* */
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    @VisibleForTesting
    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
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
