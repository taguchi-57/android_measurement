package com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.BasePresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.BaseView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.util.List;

public interface ResisteredMediumContract {

    /**
     * 登録の英語はResisterではなくRegister    sじゃなくてgです。
     * 後々全てのスペル委を置換したいと考えています
     *
     */
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMedium(List<MediaModel> medium);

//        void showAddMedia();

        void showImageProssesingUi(String mediaId);

        void showMediaMarkedComplete();

        void showMediaMarkedActive();

        void showCompletedMediumCleared();

        void showLoadingMediumError();

        void showNoMedium();

//        void showActiveFilterLabel();
//
//        void showCompletedFilterLabel();

//        void showAllFilterLabel();

        void showNoActiveMedium();

        void showNoCompletedMedium();

        void showSuccessfullySavedMessage();

        boolean isActive();

//        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadMedium(boolean forceUpdate);

        void addNewMedia();

//        void openImageProssesing(@NonNull MediaModel requestedMedia);

        void openImageProssesing(android.view.View v, int i, String id, List<MediaModel> medium);  //Todo　Viewの種類が怪しい部分がある

        void completeMedia(@NonNull MediaModel completedMedia);

        void activateMedia(@NonNull MediaModel activeMedia);

        void clearCompletedMedium();

        Bitmap setthumbnail(String path);

//        void setFiltering(TasksFilterType requestType);

//        TasksFilterType getFiltering();
    }



}
