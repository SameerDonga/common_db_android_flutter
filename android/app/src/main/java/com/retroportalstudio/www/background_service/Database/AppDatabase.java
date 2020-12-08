package com.retroportalstudio.www.background_service.Database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.retroportalstudio.www.background_service.Database.Objects.Dog;

import java.io.File;

@Database(entities = {Dog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "/data/user/0/com.retroportalstudio.www.background_service/databases/doggie_database.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
//                        .createFromFile(new File("/data/user/0/com.retroportalstudio.www.background_service/databases/doggie_database.db"))
                        .build();

//                String backupDBPath = RoomDatabase.getDatabase(context).getOpenHelper().getWritableDatabase().getPath();


            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        Log.e(LOG_TAG, "Database path ==> "+context.getApplicationContext());

        return sInstance;
    }

    public abstract DogDao dogDao();
}