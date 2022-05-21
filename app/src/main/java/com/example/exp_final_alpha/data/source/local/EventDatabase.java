package com.example.exp_final_alpha.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.exp_final_alpha.data.DateConverters;
import com.example.exp_final_alpha.data.Event;

@Database(entities = {Event.class},version = 1,exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class EventDatabase extends RoomDatabase {
    private static EventDatabase INSTANCE;
    public abstract EventDao eventDao();
    private static final Object sLock=new Object();

    public static EventDatabase getInstance(Context context){
        synchronized (sLock){
            if(INSTANCE==null){
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                        EventDatabase.class,"event.db").build();
            }
            return INSTANCE;
        }
    }

}
