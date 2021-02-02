package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.graphics.Bitmap;

import com.example.android.hrs.blueprints.jumpmeasurementapp.BasePresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.BaseView;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;

import java.util.List;

public interface SelectTemplateContract {

    // 必要関数は未精査
    interface View extends BaseView<SelectTemplateContract.Presenter> {

        void setLoadingIndicator(boolean active);

//        void showMissingMedia();

//        void settemp(int temp);
        void showLoadingTemplatesError();


        //
        void showTemplates(List<TemplateDb> templates);

        void showNoTemplates();

        void showImageProssesingUi(String template_id);
//
//        void showAddMedia();
//
//        void showMediaDetailsUi(String taskId);
//
//        void showMediaMarkedComplete();
//
//        void showTaskMarkedComplete();
//
//        void showCompletedMediumCleared();
//
//        void showLoadingMediumError();
        //
//        void showNoMedium();
//
//        void showNoActiveMedium();
//
//        void showNoCompletedMedium();
//
//        void showSuccessfullySavedMessage();
//
//        void showMediaResistered();
//
//        boolean isActive();
//
//        void showMediumList();
////
////        void showFilteringPopUpMenu();



    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

//        void loadMedium(boolean forceUpdate);

        void loadTemplates(boolean forceUpdate);

        void openImageProssesing(android.view.View v, int i, String template_id, List<TemplateDb> templates);
    }



}
