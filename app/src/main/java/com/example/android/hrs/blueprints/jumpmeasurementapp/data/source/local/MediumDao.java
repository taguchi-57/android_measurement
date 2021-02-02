package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

import java.util.List;
@Dao
public interface MediumDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all medium.
     */
    @Query("SELECT * FROM Medium")
    List<MediaModel> getMedium();

    /**
     * Select a task by id.
     *
     * @param mediaId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Medium WHERE entryid = :mediaId")
    MediaModel getMediaById(String mediaId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param mediamodel the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedia(MediaModel mediamodel);

    /**
     * Update a media.
     *
     * @param media media to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    int updateMedia(MediaModel media);

//    /**
//     * Update the complete status of a task
//     *
//     * @param taskId    id of the task
//     * @param completed status to be updated
//     */
//    @Query("UPDATE Medium SET completed = :completed WHERE entryid = :taskId")
//    void updateCompleted(String taskId, boolean completed);

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM Medium WHERE entryid = :mediaId")
    int deleteMediaById(String mediaId);

    /**
     * Delete all Medium.
     */
    @Query("DELETE FROM Medium")
    void deleteMedium();

//    /**
//     * Delete all completed tasks from the table.
//     *
//     * @return the number of tasks deleted.
//     */
//    @Query("DELETE FROM Tasks WHERE completed = 1")
//    int deleteCompletedTasks();
}
