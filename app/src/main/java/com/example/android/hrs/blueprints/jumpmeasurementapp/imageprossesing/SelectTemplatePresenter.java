package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.TemplateDataSorce;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.TemplateRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium.ResisteredMediumContract;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SelectTemplatePresenter implements  SelectTemplateContract.Presenter{

    private final TemplateRepository mMediumRepository;
    private final SelectTemplateContract.View mTemplatesView;
    private String mId;

//    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public SelectTemplatePresenter(@NonNull String id,@NonNull TemplateRepository templatesRepository, @NonNull SelectTemplateContract.View templatesView) {
        mId = id;
        mMediumRepository = checkNotNull(templatesRepository, "tasksRepository cannot be null");
        mTemplatesView = checkNotNull(templatesView, "tasksView cannot be null!");

        mTemplatesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTemplates(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        // 次への遷移？？はパス
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            mMediumView.showSuccessfullySavedMessage();
//        }
    }

    @Override
    public void loadTemplates(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        // ToDo 最初にネットワークnetwork通してデータを取得しようとすると、エラーで落ちる
        // ToDo 本来ネットワークで取得できなかった場合、自動的にローカルを取得しているものとも持っているが、
        // ToDo  今の段階ではエラーでローカルデータを身に行けていない？？？
//        loadMedium(forceUpdate || mFirstLoad, true);
        loadMedium(false, true);
        mFirstLoad = false;
    }

    @Override
    public void openImageProssesing(View v , int i, String template_id, List<TemplateDb> medium) {
        mTemplatesView.showImageProssesingUi(medium.get(i).getId());
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MediumDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadMedium(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTemplatesView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mMediumRepository.refreshTemplates();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mMediumRepository.getTemplates(new TemplateDataSorce.LoadTemplateCallBack() {
            @Override
            public void onTemplateLoaded(List<TemplateDb> templates) {
                List<TemplateDb> templatesToShow = new ArrayList<TemplateDb>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                for (TemplateDb template : templates) {

                    templatesToShow.add(template);
                }

                // The view may not be able to handle UI updates anymore
//                if (!mTemplatesView.isActive()) {
//                    return;
//                }
                if (showLoadingUI) {
                    mTemplatesView.setLoadingIndicator(false);
                }

                processTemplates(templatesToShow);
            }

            @Override
            public void onDataNotAvailable() {
                mTemplatesView.showLoadingTemplatesError();
            }
        });
    }

    private void processTemplates(List<TemplateDb> templates) {
        if (templates.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyMedium();
        } else {
            // Show the list of tasks
            mTemplatesView.showTemplates(templates);
            // Set the filter label's text.
//            showFilterLabel();
        }
    }

//    private void showFilterLabel() {
//        switch (mCurrentFiltering) {
//            case ACTIVE_TASKS:
//                mTasksView.showActiveFilterLabel();
//                break;
//            case COMPLETED_TASKS:
//                mTasksView.showCompletedFilterLabel();
//                break;
//            default:
//                mTasksView.showAllFilterLabel();
//                break;
//        }
//    }

    private void processEmptyMedium() {
//        switch (mCurrentFiltering) {
//            case ACTIVE_TASKS:
//                mTasksView.showNoActiveTasks();
//                break;
//            case COMPLETED_TASKS:
//                mTasksView.showNoCompletedTasks();
//                break;
//            default:
//                mTasksView.showNoTasks();
//                break;
//        }
        mTemplatesView.showNoTemplates();
    }

//    @Override
//    public void addNewMedia() {
////        mMediumView.showAddMedia();
//    }
//
//    @Override
//    public void openImageProssesing(View v , int i, String id, List<MediaModel> medium) {
//        // ToDo NUllチェックを入れるべきだ
////        checkNotNull(medium, "requestedTask cannot be null!");
//        mTemplatesView.showImageProssesingUi(medium.get(i).getId());
//    }

}
