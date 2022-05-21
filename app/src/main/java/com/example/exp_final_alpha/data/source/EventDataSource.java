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

    void getEvents(@NonNull LoadEventsCallback callback);

    void getEvent(@NonNull String eventName,@NonNull GetEventCallback callback);

    void getEvents(@NonNull int eventType,@NonNull LoadEventsCallback callback);

    void saveEvent(@NonNull Event event);

    void deleteEvent(@NonNull String eventName);

    void completeEvent(@NonNull String eventName);

    void activateEvent(@NonNull String eventName);

    void enableRepeatEvent(@NonNull String eventName);

    void cancelRepeatEvent(@NonNull String eventName);




}
