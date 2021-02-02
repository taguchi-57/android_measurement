package com.example.android.hrs.blueprints.jumpmeasurementapp.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakeMediumRemoteDataSource implements MediumDataSource {


    private static FakeMediumRemoteDataSource INSTANCE;

    private static final Map<String, MediaModel> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeMediumRemoteDataSource() {}

    public static FakeMediumRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeMediumRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getMedium(@NonNull LoadMediumCallBack callback) {
        callback.onMediumLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override
    public void getMedia(@NonNull String mediaId, @NonNull GetMediaCallBack callback) {
        MediaModel media = TASKS_SERVICE_DATA.get(mediaId);
        callback.onMediaLoaded(media);
    }

    @Override
    public void saveMedia(@NonNull MediaModel media) {
        TASKS_SERVICE_DATA.put(media.getId(), media);
    }

//    @Override
//    public void completeTask(@NonNull MediaModel media) {
//        Task completedTask = new Task(media.getTitle(), media.getDescription(), media.getId(), true);
//        TASKS_SERVICE_DATA.put(media.getId(), completedTask);
//    }
//
//    @Override
//    public void completeTask(@NonNull String taskId) {
//        // Not required for the remote data source.
//    }
//
//    @Override
//    public void activateTask(@NonNull Task task) {
//        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
//        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
//    }
//
//    @Override
//    public void activateTask(@NonNull String taskId) {
//        // Not required for the remote data source.
//    }

//    @Override
//    public void clearCompletedTasks() {
//        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Task> entry = it.next();
//            if (entry.getValue().isCompleted()) {
//                it.remove();
//            }
//        }
//    }

    public void refreshMedium() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteMedia(@NonNull String mediaId) {
        TASKS_SERVICE_DATA.remove(mediaId);
    }

    @Override
    public void deleteAllMedium() {
        TASKS_SERVICE_DATA.clear();
    }

    @VisibleForTesting
    public void addMedium(MediaModel... medium) {
        for (MediaModel media : medium) {
            TASKS_SERVICE_DATA.put(media.getId(), media);
        }
    }

}
