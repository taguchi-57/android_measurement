package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.util.List;

public interface MediumDataSource {

    interface  LoadMediumCallBack {

        void onMediumLoaded(List<MediaModel> medium);   //ここ怪しい引数？？の部分

        void onDataNotAvailable();

    }

    interface GetMediaCallBack {

        void onMediaLoaded(MediaModel media);   //ここ怪しい引数？？の部分

        void onDataNotAvailable();

    }

    void getMedium(@NonNull LoadMediumCallBack callback);

    void getMedia(@NonNull String mediaId, @NonNull GetMediaCallBack callback);

    void saveMedia(@NonNull MediaModel media);

    void refreshMedium();

    void deleteAllMedium();

    void deleteMedia(@NonNull String mediaId);

//    boolean CheckCachedMedia();


}
