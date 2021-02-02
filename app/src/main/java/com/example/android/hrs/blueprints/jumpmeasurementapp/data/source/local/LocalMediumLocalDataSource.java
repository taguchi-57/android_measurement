package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Dao;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.LocalMediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalMediumLocalDataSource implements LocalMediumDataSource {
    private static volatile LocalMediumLocalDataSource INSTANCE;

    private MediumDao mMediumDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private LocalMediumLocalDataSource(@NonNull AppExecutors appExecutors,
                                       @NonNull MediumDao mediumDao) {
        mAppExecutors = appExecutors;
        mMediumDao = mediumDao;
    }

    public static LocalMediumLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                         @NonNull MediumDao mediumDao) {
        if (INSTANCE == null) {
            synchronized (LocalMediumLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalMediumLocalDataSource(appExecutors, mediumDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadMediumCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getMedium() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                final List<LocalMedia> medium = mMediumDao.getMedium();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
//                        if (medium.isEmpty()) {
//                            // This will be called if the table is new or just empty.
//                            callback.onDataNotAvailable();
//                        } else {
//                            callback.onMediumLoaded(medium);
//                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetMediaCallback#onDataNotAvailable()} is fired if the {@link } isn't
     * found.
     */
    @Override
    public void getMedia(@NonNull final String mediaId, @NonNull final GetMediaCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // ここでmediaから持ってくるのではないだろう。
//                final LocalMedia media = mMediumDao.getMediaById(mediaId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
//                        if (media != null) {
//                            callback.onMediaLoaded(media);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    //　セーブはSQLiteのデータベースにセーブする必要がある。のでMediaModelクラスを使う？？？？
    @Override
    public void saveMedia(@NonNull final MediaModel media) {
        checkNotNull(media);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mMediumDao.insertMedia(media);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    //多分要らないだろうと思う
//    @Override
//    public void completeMedia(@NonNull final Task task) {
//        Runnable completeRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTasksDao.updateCompleted(task.getId(), true);
//            }
//        };
//
//        mAppExecutors.diskIO().execute(completeRunnable);
//    }

//    @Override
//    public void completeMedia(@NonNull String mediaId) {
//        // Not required for the local data source because the {@link TasksRepository} handles
//        // converting from a {@code taskId} to a {@link task} using its cached data.
//    }

    //ローカルファイルにアクティブもk非アクティブもないはずなのでこれはいらないだろうと思う。
//    @Override
//    public void activateTask(@NonNull final Task task) {
//        Runnable activateRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTasksDao.updateCompleted(task.getId(), false);
//            }
//        };
//        mAppExecutors.diskIO().execute(activateRunnable);
//    }

    // ローカルファイルなので同上
//    @Override
//    public void activateTask(@NonNull String taskId) {
//        // Not required for the local data source because the {@link TasksRepository} handles
//        // converting from a {@code taskId} to a {@link task} using its cached data.
//    }

    // ローカルファイルなので同上
//    @Override
//    public void clearCompletedTasks() {
//        Runnable clearTasksRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTasksDao.deleteCompletedTasks();
//
//            }
//        };
//
//        mAppExecutors.diskIO().execute(clearTasksRunnable);
//    }

    @Override
    public void refreshMedium() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }


    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }

//    @Override
//    public boolean CheckCachedMedia() {
//      //
//    }
}
