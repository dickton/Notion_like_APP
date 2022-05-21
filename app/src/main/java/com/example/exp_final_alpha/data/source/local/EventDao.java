package com.example.exp_final_alpha.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.exp_final_alpha.data.DateConverters;
import com.example.exp_final_alpha.data.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    List<Event> getEvents();

    @Query("SELECT * FROM events WHERE eventName= :eventName")
    Event getEventsByName(String eventName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);

    @Query("SELECT * FROM events WHERE repeat=1 AND completed=0 AND startDate<=:currentTime AND dueDate>:currentTime")
    List<Event> getRepeatEvents(long currentTime);

    @Query("SELECT * FROM events WHERE startDate>:currentTime")
    List<Event> getNextUpEvents(long currentTime);

    @Query("SELECT * FROM events WHERE repeat=0 AND completed!=0 AND startDate=NULL AND dueDate=NULL")
    List<Event> getNoStatusEvents();

    @Query("SELECT * FROM events WHERE completed!=0 AND startDate<=:currentTime AND dueDate>:currentTime AND repeat!=0")
    List<Event> getInProgressEvents(long currentTime);

    @Query("SELECT * FROM events WHERE completed=1 OR dueDate<:currentTime")
    List<Event> getCompletedOrOverdueEvents(long currentTime);

    @Query("UPDATE events SET completed= :completed WHERE eventName=:eventName")
    void updateCompleted(String eventName,boolean completed);

    @Query("DELETE FROM events WHERE eventName=:eventName")
    int deleteEventByEventName(String eventName);

    @Query("DELETE FROM events")
    void deleteEvents();

    @Query("DELETE FROM events WHERE completed= 1")
    int deleteCompletedEvents();

    @Query("UPDATE events SET needAlarm=:needAlarm WHERE eventName=:eventName")
    void updateAlarm(String eventName,boolean needAlarm);

    @Query("UPDATE events SET startDate=:startDate WHERE eventName=:eventName")
    void updateStartDate(String eventName,long startDate);

    @Query("UPDATE events SET repeat=:repeat WHERE eventName=:eventName")
    void updateRepeat(String eventName,boolean repeat);



}
