package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalMediumRepository implements LocalMediumDataSource{

    private static LocalMediumRepository INSTANCE = null;
    private final LocalMediumDataSource mMediumLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, LocalMedia> mCachedMedium;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private LocalMediumRepository(@NonNull LocalMediumDataSource mediumLocalDataSource) {
        mMediumLocalDataSource = checkNotNull(mediumLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     * @param mediumLocalDataSource  the device storage data source
     * @return the {@link LocalMediumRepository} instance
     */
    public static LocalMediumRepository getInstance(LocalMediumDataSource mediumLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new LocalMediumRepository(mediumLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadMediumCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getMedium() {
//        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
//        if (mCachedMedium != null && !mCacheIsDirty) {
        if (mCachedMedium != null) {
//            callback.onMediumLoaded(new ArrayList<>(mCachedMedium.values()));
            return;
        }

        // Do in memory cache update to keep the app UI up to date
        // TODO:おそらくここでキャッシュのリストに初期化、的な処理をしなければ、おそらくエラーが出るとみています。
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        getMediumFromLocalDataSource();
    }







    @Override
    public void saveMedia(@NonNull MediaModel media) {
        checkNotNull(media);
        mMediumLocalDataSource.saveMedia(media);

    }

//    @Override
    public void editActiveMode(@NonNull LocalMedia media) {
        checkNotNull(media);

        LocalMedia completedMedia = new LocalMedia(media.getMediaPath(), media.getId(), true);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.put(media.getId(), completedMedia);
    }

    public void endEditActiveMode(@NonNull LocalMedia media) {
        checkNotNull(media);
//        mMediumRemoteDataSource.completeMedia(media);
//        mMediumLocalDataSource.completeMedia(media);

        LocalMedia completedMedia = new LocalMedia(media.getMediaPath(), media.getId(), false);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.put(media.getId(), completedMedia);
    }


    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetMediaCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getMedia(@NonNull final String mediaId, @NonNull final GetMediaCallback callback) {
        checkNotNull(mediaId);
        checkNotNull(callback);

        LocalMedia cachedMedia = getMediaWithId(mediaId);

        // Respond immediately with cache if available
        if (cachedMedia != null) {
            callback.onMediaLoaded(cachedMedia);
            return;
        }
    }

    @Override
    public void refreshMedium() {
        mCacheIsDirty = true;
    }


    private void getMediumFromLocalDataSource() {

//        mCachedMediumに入れていく
        File directory = new File(Environment.getExternalStorageDirectory() + "/Movies");
        File[] files = directory.listFiles();

        if(files != null && files.length > 0 ) {
            for (File f : files) {
                // 拡張子を取得
                String extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                // 表示
//                System.out.println("拡張子：" + extension);
                String a = ".mp4";
                if(extension.equals(a)){
                    LocalMedia newMedia = new LocalMedia(f.getAbsolutePath());
                    mCachedMedium.put(newMedia.getId(), newMedia);
                }
            }
        }
    }




    // interfaceの@Overrideはいらない？？そもそもOverrideの存在意義は？？
    public void refreshCache(List<LocalMedia> medium) {
        if (mCachedMedium == null) {
            mCachedMedium = new LinkedHashMap<>();
        }
        mCachedMedium.clear();
        for (LocalMedia media : medium) {
            LocalMedia tempMedia = new LocalMedia(media.getMediaPath(), media.getId(), false);
            mCachedMedium.put(media.getId(), tempMedia);
        }
    }

    @Nullable
    private LocalMedia getMediaWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedMedium == null || mCachedMedium.isEmpty()) {
            return null;
        } else {
            return mCachedMedium.get(id);
        }
    }

    //インターフェースから対応する必要があることはわかるが、どうしたら
//    @Override
    public boolean CheckCachedMedia() {
        return mCachedMedium.isEmpty();
        // データがマップされているのか確認
    }

    //  リストのインターフェースの使い方がよくわからない。
    public  List<LocalMedia> getMediumList() {
        return new ArrayList<>(mCachedMedium.values());
    }
}

