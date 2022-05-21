package com.example.exp_final_alpha.events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.source.local.EventDatabase;
import com.example.exp_final_alpha.data.source.local.EventLocalDataSource;
import com.example.exp_final_alpha.util.ActivityUtils;
import com.example.exp_final_alpha.util.AppExecutors;
import com.google.android.material.navigation.NavigationView;

public class EventsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private EventsPresenter eventsPresenter;

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);



        imageView=findViewById(R.id.events_head_image);
        imageView.setImageResource(R.drawable.default_cover);

        //设置drawer
        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);

        EventsFragment eventsFragment=(EventsFragment) getSupportFragmentManager().
                findFragmentById(R.id.content_frame);
        if(eventsFragment==null){
            eventsFragment=EventsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), eventsFragment,R.id.content_frame);
        }

        EventDatabase database=EventDatabase.getInstance(getApplicationContext());
        EventLocalDataSource source=EventLocalDataSource.getInstance(new AppExecutors(),database.eventDao());
        eventsPresenter=new EventsPresenter(eventsFragment,source);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        eventsPresenter.notifyRestart();
    }
}