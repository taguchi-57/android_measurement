package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;

/**
 * The Room Database that contains the Media table.
 */
@Database(entities = {MediaModel.class}, version = 1)
public abstract class MediumDatabase  extends RoomDatabase{

    private static MediumDatabase INSTANCE;

    public abstract MediumDao mediumDao();

    private static final Object sLock = new Object();

    public static MediumDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MediumDatabase.class, "Medium.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
