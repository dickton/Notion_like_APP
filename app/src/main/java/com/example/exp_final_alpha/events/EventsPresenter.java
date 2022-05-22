package com.example.exp_final_alpha.events;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.data.source.EventDataSource;
import com.example.exp_final_alpha.data.source.local.EventLocalDataSource;
import com.example.exp_final_alpha.eventDetail.EventDetailActivity;
import com.example.exp_final_alpha.events.eventsList.ChildRecyclerViewAdapter;
import com.example.exp_final_alpha.events.eventsList.RecyclerViewItem;
import com.example.exp_final_alpha.util.EspressoIdlingResource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EventsPresenter implements EventsContract.Presenter{

    private final EventsContract.View eventsView;

    private final EventLocalDataSource eventLocalDataSource;

    private boolean firstLoad=true;

    private long currentTime;

    private final static int NO_STATUS=0;
    private final static int NEXT_UP=1;
    private final static int IN_PROGRESS=2;
    private final static int LONG_TERM=3;
    private final static int DONE_OR_OVERDUE=4;


    public EventsPresenter(@NonNull EventsContract.View eventsView, @NonNull EventLocalDataSource eventLocalDataSource) {
        this.eventsView = eventsView;
        this.eventLocalDataSource = eventLocalDataSource;
        this.currentTime=System.currentTimeMillis();
        this.eventsView.setPresenter(this);
    }

    @Override
    public void start() {
        if(eventsView.isAppFirstRun()){
            generateSampleInfo();
        }
        loadEvents(false);
    }

    /*
    @Override
    public void result(int requestCode, int resultCode) {
        if (EventDetailActivity.REQUEST_ADD_EVENT == requestCode && Activity.RESULT_OK == resultCode) {
            eventsView.showSuccessfullySavedMessage();
        }
    }

     */

    @Override
    public void loadEvents(boolean forceUpdate) {
        privateLoadEvents(forceUpdate||firstLoad);
        firstLoad=false;
    }

    private void privateLoadEvents(boolean forceUpdate){

        EspressoIdlingResource.increment();

        eventLocalDataSource.getEvents(new EventDataSource.LoadEventsCallback() {

            @Override
            public void onEventsLoaded(List<Event> events) {
                List<Event> eventsToShow=new ArrayList<Event>();
                if(!EspressoIdlingResource.getIdlingResource().isIdleNow()){
                    EspressoIdlingResource.decrement();
                }
                /* Can apply Filters at here
                for(Event event:events){

                    eventsToShow.add(event);
                }
                */
                eventsToShow.addAll(events);

                if(!eventsView.isActive())
                    return;
                processEvents(eventsToShow);
            }


            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void processEvents(List<Event> events){
        if(events.isEmpty()){
            processEmptyEvents();
        }else {
            sortEvents(events);
        }
    }

    @Override
    public void sortEvents(List<Event> events) {
        List<Event>[] lists =new ArrayList[5];
        for(int i=0;i<5;i++){
            lists[i]=new ArrayList<>();
        }
        for(Event event:events){
            lists[getEventType(event)].add(event);
            Log.d("EVENT_FRAG", event.getEventName());
        }
        Log.d("EVENT_PRESENTER","Lists sorted!");
        eventsView.addEventListsToAdapter(lists);
    }

    private int getEventType(Event event){
        if(event.isEventCompleted()||event.getEventDuetDate().getTime()<currentTime) {
            return DONE_OR_OVERDUE;
        }
        if(event.getEventStartDate().getTime()>currentTime){
            return NEXT_UP;
        }
        if(event.getEventStartDate().getTime()<=currentTime) {
            if (event.isEventRepeat())
                return LONG_TERM;
            else
                return IN_PROGRESS;
        }
        return NO_STATUS;
    }

    private void processEmptyEvents(){

    }

    @Override
    public void addNewEvent() {
        eventsView.showAddEvent();
        Log.d("EVENT_FRAG", "Presenter:addNewEvent() called!");
    }

    @Override
    public void openEventDetails(@NonNull String eventName) {
        eventsView.showEventDetailsUi(eventName);
    }

    @Override
    public void clearOverdueEvents() {

    }

    @Override
    public void notifyRestart() {
        loadEvents(true);
    }

    public void generateSampleInfo(){
        Event overdueSample=new Event("Python",Date.valueOf("2021-2-1"),
                Date.valueOf("2022-5-1"),true,false,false,0,null);
        eventLocalDataSource.saveEvent(overdueSample);

        Event overdueSample2=new Event("C++ course",Date.valueOf("2020-9-1"),
                Date.valueOf("2021-1-31"),true,false,false,0,null);
        eventLocalDataSource.saveEvent(overdueSample2);

        Event longTermSample=new Event("Learning",Date.valueOf("2000-1-1"),
                Date.valueOf("2030-1-1"),false,true,false,1,null);
        eventLocalDataSource.saveEvent(longTermSample);

        Event nextUpSample1=new Event("final exam 1",Date.valueOf("2023-6-23"),Date.valueOf("2023-6-27"),false,false,false,2,null);
        eventLocalDataSource.saveEvent(nextUpSample1);

        Event nextUpSample2=new Event("final exam 2",Date.valueOf("2023-6-23"),Date.valueOf("2023-6-27"),false,false,false,2,null);
        eventLocalDataSource.saveEvent(nextUpSample2);

        Event nextUpSample3=new Event("final exam 3",Date.valueOf("2023-6-23"),Date.valueOf("2023-6-27"),false,false,false,2,null);
        eventLocalDataSource.saveEvent(nextUpSample3);

        Event inProgressSample1=new Event("Android Learn",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample1);

        Event inProgressSample2=new Event("Course 2",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample2);

        Event inProgressSample3=new Event("Web course",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample3);

        Event inProgressSample4=new Event("Machine Learning",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample4);

        Event inProgressSample5=new Event("水课 1",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample5);

        Event inProgressSample6=new Event("水课 2",Date.valueOf("2022-3-1"),Date.valueOf("2022-7-20"),false,false,false,1,null);
        eventLocalDataSource.saveEvent(inProgressSample6);

    }
}
