package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediumLocalDataSource implements MediumDataSource {



    private static volatile MediumLocalDataSource INSTANCE;

    private MediumDao mMediumDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private MediumLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull MediumDao tasksDao) {
        mAppExecutors = appExecutors;
        mMediumDao = tasksDao;
    }

    public static MediumLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull MediumDao mediumDao) {
        if (INSTANCE == null) {
            // 排他制御　　https://qiita.com/leebon93/items/c7f2ac357f36930ff77f
            synchronized (MediumLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MediumLocalDataSource(appExecutors, mediumDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link MediumDataSource.LoadMediumCallBack#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getMedium(@NonNull final MediumLocalDataSource.LoadMediumCallBack callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<MediaModel> medium = mMediumDao.getMedium();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (medium.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onMediumLoaded(medium);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }


    /**
     * Note: {@link MediumDataSource.GetMediaCallback#onDataNotAvailable()} is fired if the {@link Task} isn't
     * found.
     */
    @Override
    public void getMedia(@NonNull final String mediaId, @NonNull final MediumDataSource.GetMediaCallBack callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final MediaModel media = mMediumDao.getMediaById(mediaId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (media != null) {
                            callback.onMediaLoaded(media);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

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

    @Override
    public void refreshMedium() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllMedium() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mMediumDao.deleteMedium();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteMedia(@NonNull final String mediaId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mMediumDao.deleteMediaById(mediaId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }


//    /**
//     * Note: {@link TasksDataSource.GetTaskCallback#onDataNotAvailable()} is fired if the {@link MediaModel} isn't
//     * found.
//     */
//    @Override
//    public void getMedia(@NonNull final String mediaId, @NonNull final GetMediaCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final MediaModel media = mMediumDao.getTaskById(mediaId);
//
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (media != null) {
//                            callback.onMediaLoaded(media);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
//    }
}
