package com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.BasePresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.BaseView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;

import java.util.List;

public interface MediumSelectContract {


    // 必要関数は未精査
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMedium(List<LocalMedia> medium);

        void showAddMedia();

        void showMediaDetailsUi(String taskId);

        void showMediaMarkedComplete();

        void showTaskMarkedComplete();

        void showCompletedMediumCleared();

        void showLoadingMediumError();

        void showNoMedium();

        void showNoActiveMedium();

        void showNoCompletedMedium();

        void showSuccessfullySavedMessage();

        void showMediaResistered();

        boolean isActive();

        void showMediumList();
//
//        void showFilteringPopUpMenu();



    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadMedium(boolean forceUpdate);

        void addNewMedia();

        void editDetails(@NonNull LocalMedia requestedMedia);

        void endEditMedia(@NonNull LocalMedia completedMedia);

        void saveMedia(String mediaPath,String title, String date);

        void endAllEditMode(List<LocalMedia> medium);

        Bitmap setthumbnail(String path);

    }



}
