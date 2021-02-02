package com.example.android.hrs.blueprints.jumpmeasurementapp.mediumselect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaDataSource;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.LocalMedia;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.LocalMediumDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.LocalMediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.EspressoIdlingResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediumSelectPresenter implements MediumSelectContract.Presenter {

    private final LocalMediumRepository mMediumRepository;

    private final MediumSelectContract.View mMediumView;

//    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public MediumSelectPresenter(@NonNull LocalMediumRepository mediumRepository, @NonNull MediumSelectContract.View mediumView) {
        mMediumRepository = checkNotNull(mediumRepository, "tasksRepository cannot be null");
        mMediumView = checkNotNull(mediumView, "tasksView cannot be null!");

        mMediumView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMedium (false);
    }

    @Override
    public void saveMedia(String mediaPath,String title, String date) {
        //アップデートと新規作成方法は要検討
//        if (isNewTask()) {
//            createMedia(title, description);
//        } else {
//            updateMedia(title, description);
//        }
        createMedia(mediaPath,title,date);
    }

    private void createMedia(String mediapath,String title, String date) {
        MediaModel newMedia = new MediaModel(mediapath,title,date);
        if (newMedia.isEmpty()) {
//            mAddTaskView.showEmptyTaskError();
        } else {
            mMediumRepository.saveMedia(newMedia);
            mMediumView.showMediaResistered();
            mMediumView.showMediumList();
        }
    }



    @Override
    public void result(int requestCode, int resultCode) {
//        // If a task was successfully added, show snackbar
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            mMediumView.showSuccessfullySavedMessage();
//        }
    }

    @Override
    public void loadMedium(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadMedium(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadMedium(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
//            mMediumView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mMediumRepository.refreshMedium();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mMediumRepository.getMedium();

        processMedium();

    }

    private void processMedium() {
        if (mMediumRepository.CheckCachedMedia()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyMedium();
        } else {
            // Show the list of tasks
            List<LocalMedia> mediumToShow = new ArrayList<LocalMedia>();
            // getMediumListでList<LocalMedia>のかたちでデータを入れる。
            List<LocalMedia> medium = mMediumRepository.getMediumList();
            for (LocalMedia media : medium) {
                mediumToShow.add(media);
            }

            mMediumView.showMedium(mediumToShow);
            // Set the filter label's text.
//            showFilterLabel();
        }
    }

    private void processEmptyMedium() {

        mMediumView.showNoMedium();

    }

    @Override
    public void addNewMedia() {
        mMediumView.showAddMedia();
    }

    @Override
    public void editDetails(@NonNull LocalMedia noActiveMedia) {
        checkNotNull(noActiveMedia, "completedTask cannot be null!");
        mMediumRepository.editActiveMode(noActiveMedia);
        mMediumView.showTaskMarkedComplete();
        loadMedium(false, false);
    }

    @Override
    public void endEditMedia(@NonNull LocalMedia activeMedia) {
        checkNotNull(activeMedia, "completedTask cannot be null!");
        mMediumRepository.endEditActiveMode(activeMedia);
        mMediumView.showMediaMarkedComplete();
        loadMedium(false, false);
    }

    @Override
    public void endAllEditMode(List<LocalMedia> medium) {
        checkNotNull(medium);
        mMediumRepository.refreshCache(medium);
        mMediumView.showMediaMarkedComplete();
        loadMedium(false, false);
    }

    @Override
    public Bitmap setthumbnail(String path){
        Bitmap bmp = getFirstFrame(path);
        return bmp;
    }

    private Bitmap getFirstFrame(String path){
        Bitmap bmp = null;
        FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();

//        File file = new File(path);
        float mount_frame_time = 0.0f;

//        if (file.exists())
//        {
//            File file2 = new File(path);

//            if(file2.exists())
//            {
                med.setDataSource(path);

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
//            }
//        }
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
