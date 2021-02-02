package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.util.Collection;
import java.util.List;

public interface LocalMediumDataSource {

    interface LoadMediumCallback {

        void onMediumLoaded(List<LocalMedia> tasks);

        void onDataNotAvailable();
    }

    interface GetMediaCallback {

        void onMediaLoaded(LocalMedia media);

        void onDataNotAvailable();
    }

    void getMedium();

    void getMedia(@NonNull String taskId, @NonNull GetMediaCallback callback);

    // ここだけSQLiteのメディアモデルに追加したい。
    void saveMedia(@NonNull MediaModel media);

//    void completeMedia(@NonNull LocalMedia media);

//    void completeMedia(@NonNull String mediaId);

//    void activateMedia(@NonNull LocalMedia media);

//    void activateMedia(@NonNull String mediaId);

//    void clearCompletedMedium();

    void refreshMedium();

//    void editOnActiveMedia();

//    boolean CheckCachedMedia();

//    List<MediaModel> getMediumList();
}
