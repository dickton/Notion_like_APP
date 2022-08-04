package com.example.exp_final_alpha.events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.source.local.EventDatabase;
import com.example.exp_final_alpha.data.source.local.EventLocalDataSource;
import com.example.exp_final_alpha.util.ActivityUtils;
import com.example.exp_final_alpha.util.AppExecutors;
import com.google.android.material.navigation.NavigationView;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;

    private EventsPresenter eventsPresenter;

    private ImageView imageView;

    private Button btn_change_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        btn_change_cover = findViewById(R.id.btn_change_cover);
        btn_change_cover.setOnClickListener(this);

        imageView = findViewById(R.id.events_head_image);
        //imageView.setImageResource(R.drawable.default_cover);
        getCover();
        //设置drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        EventsFragment eventsFragment = (EventsFragment) getSupportFragmentManager().
                findFragmentById(R.id.content_frame);
        if (eventsFragment == null) {
            eventsFragment = EventsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), eventsFragment, R.id.content_frame);
        }

        EventDatabase database = EventDatabase.getInstance(getApplicationContext());
        EventLocalDataSource source = EventLocalDataSource.getInstance(new AppExecutors(), database.eventDao());
        eventsPresenter = new EventsPresenter(eventsFragment, source);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        eventsPresenter.notifyRestart();
    }

    private void getCover() {
        SharedPreferences base = getSharedPreferences("base", Context.MODE_PRIVATE);
        String imageUri = base.getString("imageUri", "");
        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.default_cover);
        } else {
            Uri uri = Uri.parse(imageUri);
            imageView.setImageURI(uri);
        }
    }

    private void changeCover() {
        Toast.makeText(this,"change cover clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_cover:
                changeCover();
                break;
            default:
                break;
        }
    }
}