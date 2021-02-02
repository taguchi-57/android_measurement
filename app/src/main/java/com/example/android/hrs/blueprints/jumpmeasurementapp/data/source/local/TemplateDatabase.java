package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;

/**
 * The Room Database that contains the Media table.
 */
@Database(entities = {TemplateDb.class}, version = 1)
public abstract class TemplateDatabase  extends RoomDatabase {

    private static TemplateDatabase INSTANCE;

    public abstract DB_TemplateDao db_templateDao();

    private static final Object sLock = new Object();

    // Todo 29行目
    public static TemplateDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TemplateDatabase.class, "templateMedia.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
