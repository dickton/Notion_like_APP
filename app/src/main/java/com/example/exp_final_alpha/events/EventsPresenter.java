package com.example.exp_final_alpha.events;

import android.app.Activity;
import android.content.Context;
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
        if(forceUpdate){

        }

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
}
