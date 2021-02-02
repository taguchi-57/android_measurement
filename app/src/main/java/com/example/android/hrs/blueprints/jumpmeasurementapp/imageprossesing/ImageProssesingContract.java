package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.BasePresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.BaseView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;
import com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect.MediumSelectContract;

import java.util.List;

public interface ImageProssesingContract {

    // 必要関数は未精査
    interface View extends BaseView<ImageProssesingContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingMedia();

        void showCropImage(String path, Bitmap bmp);

        void showCropedImage(Bitmap bmp);

        void setProgressBarHandler(int i ,double total_frame);

        void showLoadingMediumError();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void startImageProssesing(Bitmap bmp);

        void saveTemplate(Bitmap bmp);

        void showTemplate(String id);

    }
}
