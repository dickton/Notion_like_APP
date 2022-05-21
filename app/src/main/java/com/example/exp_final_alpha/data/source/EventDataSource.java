package com.example.exp_final_alpha.data.source;

import androidx.annotation.NonNull;

import com.example.exp_final_alpha.data.Event;

import java.util.List;

public interface EventDataSource {

    interface LoadEventsCallback{
        void onEventsLoaded(List<Event> events);
        void onDataNotAvailable();
    }

    interface GetEventCallback{
        void onEventLoaded(Event event);
        void onDataNotAvailable();

    }

    void getEvents(@NonNull LoadEventsCallback callback);      //读取所有事件

    void getEvent(@NonNull String eventName,@NonNull GetEventCallback callback);    //读取单个事件

    void saveEvent(@NonNull Event event);   //将数据保存到数据库中

    void deleteEvent(@NonNull String eventName);    //从数据库中删除数据

    void getEvents(@NonNull int eventType,@NonNull LoadEventsCallback callback);



    void completeEvent(@NonNull String eventName);

    void activateEvent(@NonNull String eventName);

    void enableRepeatEvent(@NonNull String eventName);

    void cancelRepeatEvent(@NonNull String eventName);




}
