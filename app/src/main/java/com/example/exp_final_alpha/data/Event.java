package com.example.exp_final_alpha.data;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.lang.reflect.Constructor;
import java.sql.Date;

@Entity(tableName="events")
@TypeConverters(DateConverters.class)
public class Event {

    public static final int PRIORITY_HIGH=2;
    public static final int PRIORITY_MEDIUM=1;
    public static final int PRIORITY_LOW=0;

    public static final String NULL_EVENT_NAME="[NULL_EVENT_NAME]";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="eventName")
    private String eventName;

    @Nullable
    @ColumnInfo(name="startDate")
    private Date eventStartDate;

    @Nullable
    @ColumnInfo(name="dueDate")
    private Date eventDuetDate;


    @ColumnInfo(name="completed")
    private boolean eventCompleted;

    @Nullable
    @ColumnInfo(name="needAlarm")
    private boolean eventNeedAlarm;


    @ColumnInfo(name="repeat")
    private boolean eventRepeat;

    @Nullable
    @ColumnInfo(name="priority")
    private int eventPriority;

    @Nullable
    @ColumnInfo(name="markdownUri")
    private String eventMarkdownUriStr;

    public boolean isEventRepeat() {
        return eventRepeat;
    }

    public void setEventRepeat(boolean eventRepeat) {
        this.eventRepeat = eventRepeat;
    }



    //Constructor
    public Event(String eventName,@Nullable Date eventStartDate,@Nullable Date eventDuetDate,
                 boolean eventCompleted,boolean eventRepeat,boolean eventNeedAlarm,
                 int eventPriority,String eventMarkdownUriStr){
        this.eventName=eventName;
        this.eventStartDate=eventStartDate;
        this.eventDuetDate=eventDuetDate;
        this.eventCompleted=eventCompleted;
        this.eventRepeat=eventRepeat;
        this.eventNeedAlarm=eventNeedAlarm;
        this.eventPriority=eventPriority;
        this.eventMarkdownUriStr=eventMarkdownUriStr;
    }

    @Nullable
    public String getEventMarkdownUriStr() {
        return eventMarkdownUriStr;
    }

    public void setEventMarkdownUriStr(@Nullable String eventMarkdownUriStr) {
        this.eventMarkdownUriStr = eventMarkdownUriStr;
    }

    public void setMarkdownEventUri(@Nullable Uri eventUri){
        this.eventMarkdownUriStr =eventUri.toString();
    }

    public Uri getMarkdownEventUri(){
        return Uri.parse(this.eventMarkdownUriStr);
    }

    @NonNull
    public String getEventName() {
        return eventName;
    }

    public void setEventName(@NonNull String eventName) {
        this.eventName = eventName;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventDuetDate() {
        return eventDuetDate;
    }

    public void setEventDuetDate(Date eventDuetDate) {
        this.eventDuetDate = eventDuetDate;
    }

    public boolean isEventCompleted() {
        return eventCompleted;
    }

    public void setEventCompleted(boolean eventCompleted) {
        this.eventCompleted = eventCompleted;
    }

    public boolean isEventNeedAlarm() {
        return eventNeedAlarm;
    }

    public void setEventNeedAlarm(boolean eventNeedAlarm) {
        this.eventNeedAlarm = eventNeedAlarm;
    }

    public int getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(int eventPriority) {
        this.eventPriority = eventPriority;
    }

    public boolean isEmpty(){
        return eventName.isEmpty();
    }


}
