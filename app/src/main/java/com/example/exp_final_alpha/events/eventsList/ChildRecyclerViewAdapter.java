package com.example.exp_final_alpha.events.eventsList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.Event;

import java.util.List;

public class ChildRecyclerViewAdapter
        extends RecyclerView.Adapter<ChildRecyclerViewAdapter.ViewHolder>{
    List<Event> items;
    onItemClickListener itemClickListener;


    public ChildRecyclerViewAdapter(List<Event> items,onItemClickListener itemClickListener){
        this.items=items;
        this.itemClickListener = itemClickListener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("EVENT_CHILD", "Child onCreateViewHolder!");
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_listview_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("EVENT_CHILD", "Child onBindViewHolder!");
        Event event=items.get(position);
        holder.setEventName(event.getEventName());
        holder.titleView.setText(event.getEventName());
        Log.d("EVENT_CHILD", event.getEventName());
    }

    @Override
    public int getItemCount() {
        return items!=null?items.size():0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        String eventName;


        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView=itemView.findViewById(R.id.events_recyclerview_brief_title);
            Log.d("EVENT_CHILD", "!");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener !=null){
                        itemClickListener.onEventClick(eventName);
                    }
                }
            });


        }
    }
    public void setOnItemClickListener(onItemClickListener listener){
        itemClickListener =listener;
    }





}
