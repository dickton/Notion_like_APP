package com.example.exp_final_alpha.eventDetail;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.data.source.EventDataSource;
import com.example.exp_final_alpha.data.source.local.EventLocalDataSource;

public class EventDetailPresenter implements EventDetailContract.Presenter, EventDataSource.GetEventCallback {

    private final EventLocalDataSource eventLocalDataSource;

    private final EventDetailContract.View eventDetailView;

    private Event event;

    private String eventName;

    private boolean loadDataFromDB;

    private boolean isNewEvent;

    private boolean nameChanged=false;

    private final static String TAG="EVENT_DETAIL";

    public EventDetailPresenter(EventLocalDataSource eventLocalDataSource,
                                EventDetailContract.View eventDetailView,
                                @Nullable String eventName) {
        this.eventLocalDataSource = eventLocalDataSource;
        this.eventDetailView = eventDetailView;
        this.eventName=eventName;
        if(eventName.equals(Event.NULL_EVENT_NAME)){
            this.loadDataFromDB=false;
            this.isNewEvent=true;
        }else {
            this.loadDataFromDB=true;
            this.isNewEvent=false;
        }
        this.eventDetailView.setPresenter(this);
        Log.d(TAG, "Presenter Constructor: presenter constructed!");
    }




    @Override
    public void saveEvent(Event event) {
        if(event.isEmpty()&&!isNewEvent) {
                eventDetailView.showEmptyEventError();
        }else{
            if(event.getEventName().isEmpty()){
                eventDetailView.showEmptyNameError();
            }
            eventLocalDataSource.saveEvent(event);
            eventDetailView.showSavedSuccessful();
            //eventDetailView.showEventsList();
        }
    }

    @Override
    public boolean isDataMissing() {
        return loadDataFromDB;
    }

    @Override
    public void populateEvent() {
        if(isNewEvent()){
            throw new RuntimeException("populateEvent() was called but the event is new");
        }
        eventLocalDataSource.getEvent(eventName,this);
    }


    public boolean isNewEvent(){
        return isNewEvent;
    }

    @Override
    public void start() {
        Log.d(TAG, "start: presenter started!");
        if(!isNewEvent()&&loadDataFromDB){
            populateEvent();
            Log.d(TAG, "start: calls populateEvent()!");
        }
    }

    @Override
    public void onEventLoaded(Event event) {
        if(eventDetailView.isActive()){
            eventDetailView.setEventInfo(event);
            Log.d("EVENT_DETAIL", "onEventLoaded: "+eventName);
            this.event=event;
        }
        loadDataFromDB=false;
    }

    @Override
    public void onDataNotAvailable() {
        if(eventDetailView.isActive()){
            eventDetailView.showEmptyEventError();
        }
    }

    public boolean changeOccur(Event event){
        if(isNewEvent&&event.getEventName().isEmpty()){
            return false;
        }

        if(isNewEvent&&!event.getEventName().isEmpty()){
            return true;
        }

        if(!this.event.getEventName().equals(event.getEventName())){
            nameChanged=true;
            Log.d(TAG, "cmpEvent: diff name!");
            return true;
        }
        if(this.event.getEventPriority()!=event.getEventPriority()){
            Log.d(TAG, "cmpEvent: diff priority!");
            return true;
        }
        if(!this.event.getEventStartDate().equals(event.getEventStartDate())){
            Log.d(TAG, "cmpEvent: diff start!");
            return true;
        }
        if(!this.event.getEventDuetDate().equals(event.getEventDuetDate())){
            Log.d(TAG, "cmpEvent: diff due!");
            return true;
        }
        if(this.event.isEventRepeat()!=event.isEventRepeat()){
            Log.d(TAG, "cmpEvent: diff repeat!");
            return true;
        }
        if(this.event.isEventNeedAlarm()!=event.isEventNeedAlarm()){
            Log.d(TAG, "cmpEvent: diff Alarm!");
            return true;
        }
        if(this.event.isEventCompleted()!=event.isEventCompleted()){
            Log.d(TAG, "cmpEvent: diff complete!");
            return true;
        }
        //if(event.getMarkdownEventUri()!=event.getMarkdownEventUri()){
            //Log.d(TAG, "cmpEvent: diff Uri!");
        //}
        return false;
    }

    @Override
    public void deleteEvent() {
        eventLocalDataSource.deleteEvent(eventName);
        eventDetailView.showEventsList();
    }

    @Override
    public void saveCurrent() {
        Event tempEvent= eventDetailView.getCurrent();
        if(tempEvent!=null){
            if(changeOccur(tempEvent)) {
                if(nameChanged&&!isNewEvent) {
                    eventLocalDataSource.deleteEvent(eventName);
                    Log.d(TAG, "saveCurrent: delete" + eventName);
                }
                saveEvent(tempEvent);
                this.event=tempEvent;
                this.eventName=tempEvent.getEventName();
                nameChanged=false;

            }else {
                eventDetailView.showNothingChanged();
            }
        }else{
            eventDetailView.showVariableEmptyError();
        }
    }
}
