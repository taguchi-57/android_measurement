package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;

import java.util.List;

public interface TemplateDataSorce {

    interface LoadTemplateCallBack {

        void onTemplateLoaded(List<TemplateDb> templateDbs);

        void onDataNotAvailable();

    }

    interface GetTemplateCallBack {

        void onTemplateLoaded(TemplateDb templateDb);

        void onDataNotAvailable();

    }

    void getTemplates(@NonNull TemplateDataSorce.LoadTemplateCallBack callback);

    void getTemplate(@NonNull String mediaId, @NonNull TemplateDataSorce.GetTemplateCallBack callback);

    void saveTemplate(@NonNull TemplateDb media);

    void refreshTemplates();

    void deleteAllTemplate();

    void deleteTemplate(@NonNull String mediaId);

}
