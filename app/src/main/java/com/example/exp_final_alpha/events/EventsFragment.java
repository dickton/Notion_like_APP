package com.example.exp_final_alpha.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.eventDetail.EventDetailActivity;
import com.example.exp_final_alpha.events.eventsList.ChildRecyclerViewAdapter;
import com.example.exp_final_alpha.events.eventsList.RecyclerViewItem;
import com.example.exp_final_alpha.events.eventsList.onCreateNewClickListener;
import com.example.exp_final_alpha.events.eventsList.onItemClickListener;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements EventsContract.View {
    private EventsContract.Presenter presenter;

    private RecyclerViewAdapter recyclerViewAdapter;

    private RecyclerView recyclerView;

    private static String[] RECYCLER_TITLES = {"No Status", "Next Up", "In Progress",
            "Long Term", "Overdue"};

    onCreateNewClickListener createNewClickListener;

    public EventsFragment(){

    }

    public static EventsFragment newInstance(){
        return new EventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.start();

        Log.d("EVENT_FRAG","Fragment created!");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.events_frag,container,false);

        recyclerView=root.findViewById(R.id.events_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter=new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        Log.d("EVENT_FRAG","onCreateView");



        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("EVENT_DETAIL", "onOptionsItemSelected: 111");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.result(requestCode,resultCode);
    }

    @Override
    public void setPresenter(@NonNull EventsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showAddEvent() {
        Log.d("EVENT_FRAG", "showAddEvent: called!");
        Intent intent=new Intent(getContext(),EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_NEW_EVENT,true);
        startActivityForResult(new Intent(getContext(),EventDetailActivity.class),
                EventDetailActivity.REQUEST_ADD_EVENT);

    }


    //启动详情Activity
    @Override
    public void showEventDetailsUi(String eventName) {
        Intent intent=new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_NAME,eventName);
        startActivity(intent);

    }

    /*
    @Override
    public void showInProgressEvents() {

    }

    @Override
    public void showNextUpEvents() {

    }

    @Override
    public void showOverdueEvents() {

    }

    @Override
    public void showRepeatEvents() {

    }

    @Override
    public void showOverdueEventsCleared() {

    }
    */

    @Override
    public void showLoadingEventsError() {
        showMsg(getString(R.string.loading_events_error));
    }

    private void showMsg(String msg){
        Snackbar.make(getView(),msg,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoEvents() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void addEventListsToAdapter(List<Event>[] lists) {
        recyclerViewAdapter.setLists(lists);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMsg(getString(R.string.successfully_saved_event_message));
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<RecyclerViewItem> items;
        private Context context;

        private onItemClickListener itemListener =new onItemClickListener() {
            //为Event注册点击监听为跳转到Event Detail服务
            @Override
            public void onEventClick(String eventName) {

                //在此处调用presenter的打开Detail方法
                presenter.openEventDetails(eventName);
                Toast.makeText(getContext(),"EventDetail!",Toast.LENGTH_SHORT).show();
            }
        };

        public RecyclerViewAdapter(Context context, List<Event>[] lists) {
            this.context = context;
            items = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                RecyclerViewItem recyclerViewItem = new RecyclerViewItem(RECYCLER_TITLES[i], lists[i]);
                items.add(recyclerViewItem);
            }
        }

        public RecyclerViewAdapter(Context context) {
            this.context = context;
            items = new ArrayList<>();
            Log.d("EVENT_ADAPTER","RecyclerViewAdapter Created!");
            for (int i = 0; i < 5; i++) {
                RecyclerViewItem recyclerViewItem = new RecyclerViewItem(RECYCLER_TITLES[i], new ArrayList<>());
                items.add(recyclerViewItem);
            }
        }

        //为内层RecyclerView设置列表
        public void setLists(List<Event>[] lists) {

            for (int i = 0; i < 5; i++) {
                items.get(i).setEventList(lists[i]);

                Log.d("EVENT_FRAG", String.format("%d",lists[i].size()));
            }
            notifyDataSetChanged();
            Log.d("EVENT_ADAPTER","Lists Set!");
        }


        //测试时使用的生成Event方法
        private List<Event> generateList(){
            List<Event> list=new ArrayList<>();
            for(int i=0;i<8;i++){
                list.add(new Event(String.format("Hello %d",i),null,null,true,true,false,1,null));
            }
            Log.d("EVENT_ADAPTER","Lists generated!");
            return list;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecyclerViewItem item = items.get(position);
            List<Event> eventsList = item.getEventList();
            //Log.d("EVENT_FRAG", String.format("%d",eventsList.size()));
            holder.textView.setText(item.getTitle());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false);

            holder.ChildRecyclerView.setAdapter(new ChildRecyclerViewAdapter(eventsList, itemListener));

            holder.ChildRecyclerView.setLayoutManager(linearLayoutManager);
            holder.createNewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.addNewEvent();
                    Log.d("EVENT_FRAG", "Create new EventActivity!");
                }
            });
            Log.d("EVENT_ADAPTER","onBindViewHolder!");
        }

        @Override
        public int getItemCount() {
            return items.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView ChildRecyclerView;
            TextView textView;
            TextView createNewView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Log.d("EVENT_ADAPTER","ViewHolder created!");
                textView = itemView.findViewById(R.id.events_recyclerview_item_txt);
                ChildRecyclerView = itemView
                        .findViewById(R.id.events_recyclerview_item_recyclerview);
                createNewView =itemView.findViewById(R.id.events_list_create_new);
            }

        }


    }
    public void setCreateNewClickListener(onCreateNewClickListener listener){
        createNewClickListener =listener;
    }

}
