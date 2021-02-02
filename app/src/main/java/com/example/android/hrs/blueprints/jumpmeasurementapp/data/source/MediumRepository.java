package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediumRepository implements MediumDataSource{

    private static MediumRepository INSTANCE = null;

    private final MediumDataSource mMediumRemoteDataSource;

    private final MediumDataSource mMediumLocalDataSource;

    /*
    * よくわからん
    * */
    Map<String, MediaModel> mCachedMedium;


    /*
    * キャッシュを無効としtrマークし、次ぎぬデータが要求された時に更新します。
    * この変数にはパッケージのローカルの可視性があるため、テストからアクセスできます
    * */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    // 直接インスタンス化を防止します。
    MediumRepository(@NonNull MediumDataSource mediumRemoteDataSource,
                     @NonNull MediumDataSource mediumLocalDataSource) {
        mMediumRemoteDataSource = checkNotNull(mediumRemoteDataSource);
        mMediumLocalDataSource = checkNotNull(mediumLocalDataSource);
    }


    /**
     * Returns the single instance of this class, creating it if necessary.
     * このクラスの単一インスタンスを返し、必要に応じて作成します。
     * 要するにシングルトン
     * @param mediumLocalDataSource the backend data source
     * @param mediumRemoteDataSource  the device storage data source
     * @return the {@link MediumRepository} instance
     */
    public static MediumRepository getInstance(MediumDataSource mediumRemoteDataSource,
                                                     MediumDataSource mediumLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MediumRepository(mediumRemoteDataSource, mediumLocalDataSource);
        }
        return INSTANCE;
    }


    /**
     * Used to force {@link #getInstance(MediumDataSource, MediumDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }



    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * キャッシュ、ローカルデータソース（SQLite）、
     * またはリモートデータソースのいずれか早い方からタスクを取得します。
     * <p>
     * Note: {@link MediumDataSource.LoadMediumCallBack#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     * 注：  {@link MediumDataSource.LoadMediumCallBack#onDataNotAvailable()}は、
     * すべてのデータソースがデータの取得に失敗した場合に発生します。
     */
    @Override
    public void getMedium(@NonNull final LoadMediumCallBack callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedMedium != null && !mCacheIsDirty) {
            callback.onMediumLoaded(new ArrayList<>(mCachedMedium.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            // キャッシュがdirtyな場合は、ネットワークから新しいデータをフェッチする必要があります。
            getMediumFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mMediumLocalDataSource.getMedium(new LoadMediumCallBack() {
                @Override
                public void onMediumLoaded(List<MediaModel> medium) {
                    refreshCache(medium);
                    callback.onMediumLoaded(new ArrayList<>(mCachedMedium.values()));
                }

                @Override
                public void onDataNotAvailable() {
//                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveMedia(@NonNull MediaModel media) {
        checkNotNull(media);
        mMediumRemoteDataSource.saveMedia(media);
        mMediumLocalDataSource.saveMedia(media);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.put(media.getId(), media);
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link MediumDataSource.GetMediaCallBack #onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getMedia(@NonNull final String mediaId, @NonNull final MediumDataSource.GetMediaCallBack callback) {
        checkNotNull(mediaId);
        checkNotNull(callback);

        MediaModel cachedMedia = getMediaWithId(mediaId);

        // Respond immediately with cache if available
        if (cachedMedia != null) {
            callback.onMediaLoaded(cachedMedia);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mMediumLocalDataSource.getMedia(mediaId, new GetMediaCallBack() {
            @Override
            public void onMediaLoaded(MediaModel media) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedMedium == null) {
                    mCachedMedium = new LinkedHashMap<>();
                }
                mCachedMedium.put(media.getId(), media);
                callback.onMediaLoaded(media);
            }

            @Override
            public void onDataNotAvailable() {
                mMediumLocalDataSource.getMedia(mediaId, new GetMediaCallBack() {
                    @Override
                    public void onMediaLoaded(MediaModel media) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedMedium == null) {
                            mCachedMedium = new LinkedHashMap<>();
                        }
                        mCachedMedium.put(media.getId(), media);
                        callback.onMediaLoaded(media);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshMedium() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllMedium() {
        mMediumRemoteDataSource.deleteAllMedium();
        mMediumLocalDataSource.deleteAllMedium();

        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.clear();
    }

    @Override
    public void deleteMedia(@NonNull String mediaId) {
        mMediumRemoteDataSource.deleteMedia(checkNotNull(mediaId));
        mMediumLocalDataSource.deleteMedia(checkNotNull(mediaId));

        mCachedMedium.remove(mediaId);
    }


    private void getMediumFromRemoteDataSource(@NonNull final LoadMediumCallBack callback) {
//      リモ－トサーバーを用いた処理だが、リモートサーバー未実装のため
        mMediumRemoteDataSource.getMedium(new LoadMediumCallBack() {
            @Override
            public void onMediumLoaded(List<MediaModel> medium) {
                refreshCache(medium);
                refreshLocalDataSource(medium);
                callback.onMediumLoaded(new ArrayList<>(mCachedMedium.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<MediaModel> medium) {
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.clear();
        for (MediaModel media : medium) {
            mCachedMedium.put(media.getId(), media);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<MediaModel> medium) {
        mMediumLocalDataSource.deleteAllMedium();
        for (MediaModel media : medium) {
            mMediumLocalDataSource.saveMedia(media);
        }
    }

    @Nullable
    private MediaModel getMediaWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedMedium == null || mCachedMedium.isEmpty()) {
            return null;
        } else {
            return mCachedMedium.get(id);
        }
    }











}
