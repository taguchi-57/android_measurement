package com.example.android.hrs.blueprints.jumpmeasurementapp.registeredmedium;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResisteredMediumPresenter  implements ResisteredMediumContract.Presenter{

    private final MediumRepository mMediumRepository;

    private final ResisteredMediumContract.View mMediumView;

//    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public ResisteredMediumPresenter(@NonNull MediumRepository mediumRepository, @NonNull ResisteredMediumContract.View mediumView) {
        mMediumRepository = checkNotNull(mediumRepository, "tasksRepository cannot be null");
        mMediumView = checkNotNull(mediumView, "tasksView cannot be null!");

        mMediumView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMedium(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {


    }

    @Override
    public void loadMedium(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        // ToDo 最初にネットワークnetwork通してデータを取得しようとすると、エラーで落ちる
        // ToDo 本来ネットワークで取得できなかった場合、自動的にローカルを取得しているものとも持っているが、
        // ToDo  今の段階ではエラーでローカルデータを身に行けていない？？？
//        loadMedium(forceUpdate || mFirstLoad, true);
        loadMedium(false, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link MediumDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadMedium(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mMediumView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mMediumRepository.refreshMedium();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mMediumRepository.getMedium(new MediumDataSource.LoadMediumCallBack() {
            @Override
            public void onMediumLoaded(List<MediaModel> medium) {
                List<MediaModel> mediumToShow = new ArrayList<MediaModel>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                for (MediaModel media : medium) {

                    mediumToShow.add(media);
                }

                // The view may not be able to handle UI updates anymore
                if (!mMediumView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mMediumView.setLoadingIndicator(false);
                }

                processMedium(mediumToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mMediumView.isActive()) {
                    return;
                }
                mMediumView.showLoadingMediumError();
            }
        });
    }

    private void processMedium(List<MediaModel> medium) {
        if (medium.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mMediumView.showMedium(medium);
            // Set the filter label's text.
//            showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        mMediumView.showNoMedium();
    }

    @Override
    public void addNewMedia() {
//        mMediumView.showAddMedia();
    }

    @Override
    public void openImageProssesing(View v , int i, String id, List<MediaModel> medium) {
        // ToDo NUllチェックを入れるべきだ
//        checkNotNull(medium, "requestedTask cannot be null!");
        mMediumView.showImageProssesingUi(medium.get(i).getId());
    }

    @Override
    public void completeMedia(@NonNull MediaModel completedMedia) {
//        checkNotNull(completedMedia, "completedTask cannot be null!");
//        mMediumRepository.completeMedia(completedMedia);
//        mMediumView.showMediaMarkedComplete();
//        loadMedium(false, false);
    }

    @Override
    public void activateMedia(@NonNull MediaModel activeMedia) {
//        checkNotNull(activeMedia, "activeTask cannot be null!");
//        mMediumRepository.activateMedia(activeMedia);
//        mMediumView.showMediaMarkedActive();
//        loadMedium(false, false);
    }

    @Override
    public void clearCompletedMedium() {
//        mMediumRepository.clearCompletedMedium();
//        mMediumView.showCompletedMediumCleared();
//        loadMedium(false, false);
    }

    @Override
    public Bitmap setthumbnail(String path){
        Bitmap bmp = getFirstFrame(path);
        return bmp;
    }

    private Bitmap getFirstFrame(String path){
        Bitmap bmp = null;
        FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();

        File file = new File(path);
        float mount_frame_time = 0.0f;

        if (file.exists())
        {
            File file2 = new File(path);

            if(file2.exists())
            {
                med.setDataSource(file2.getPath());

                String duration = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                convertMillieToHMmSs(Long.valueOf(duration));
                Log.i("i*1000000", convertMillieToHMmSs(Long.valueOf(duration)));

                //総フレーム数
                double total_frame = Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)) /1000.0  * Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
                Log.i("総フレーム数", String.valueOf(total_frame));
                for (int i = 0; i < 1; i++) {
//                    double frameRate = Double.parseDouble(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));

                    float per_frame = 1 / Float.parseFloat(med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE));
                    mount_frame_time = mount_frame_time + per_frame;
                    int int_mount_frame_time = (int)(mount_frame_time * 1000 * 1000);
                    bmp = med.getFrameAtTime(int_mount_frame_time, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);


                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    return  rotatedBitmap;
                }
            }
        }
        return bmp;


    }

    ////Util////////////////////////////////////////////////////////////////////////////////////////
    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }


}
