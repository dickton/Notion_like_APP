package com.example.exp_final_alpha.events.eventsList;

import com.example.exp_final_alpha.data.Event;

import java.util.List;

public class RecyclerViewItem {
    private String title;
    private List<Event> eventList;

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public RecyclerViewItem(String title, List<Event> eventList){
        this.title = title;
        this.eventList=eventList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
