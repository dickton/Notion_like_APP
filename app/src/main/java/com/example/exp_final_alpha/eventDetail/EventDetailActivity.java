package com.example.exp_final_alpha.eventDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.data.source.local.EventDatabase;
import com.example.exp_final_alpha.data.source.local.EventLocalDataSource;
import com.example.exp_final_alpha.util.ActivityUtils;
import com.example.exp_final_alpha.util.AppExecutors;

public class EventDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_NAME ="EVENT_NAME";
    public static final String EXTRA_NEW_EVENT ="NEW_EVENT";
    public static final int REQUEST_ADD_EVENT=1;

    private static final String TAG="EVENT_DETAIL_ACT";

    private EventDetailFragment fragment;
    private EventDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        String eventName;
        if(getIntent().hasExtra(EXTRA_EVENT_NAME)){
            eventName=getIntent().getStringExtra(EXTRA_EVENT_NAME);
        }else {
            eventName= Event.NULL_EVENT_NAME;
        }

        //Log.d("EVENT_DETAIL", eventName);

        Toolbar toolbar=findViewById(R.id.event_detail_toolbar);
        toolbar.setTitle("Event Detail");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        EventDetailFragment eventDetailFragment=(EventDetailFragment) getSupportFragmentManager().
                findFragmentById(R.id.detail_content_frame);

        if(eventDetailFragment==null){
            eventDetailFragment=EventDetailFragment.newInstance(eventName);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),eventDetailFragment,R.id.detail_content_frame);
        }
        this.fragment=eventDetailFragment;
        //Log.d(TAG, "onCreate: "+getIntent().getBooleanExtra(EXTRA_NEW_EVENT,false));
        presenter=new EventDetailPresenter(getDB(getApplicationContext()),eventDetailFragment,
                eventName);
    }

    @Override
    public void onBackPressed() {
        if(presenter.changeOccur(fragment.getCurrent())){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Notice!");
            builder.setMessage("Changes has occurred!\nPress Quit to leave without saving the changes.");
            builder.setCancelable(true);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
            return;
        }
        super.onBackPressed();
    }

    private EventLocalDataSource getDB(@NonNull Context context){
        EventDatabase database=EventDatabase.getInstance(context);
        return EventLocalDataSource.getInstance(new AppExecutors(),database.eventDao());
    }
}