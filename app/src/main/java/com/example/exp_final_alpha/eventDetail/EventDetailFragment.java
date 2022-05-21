package com.example.exp_final_alpha.eventDetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.example.exp_final_alpha.R;

import com.example.exp_final_alpha.data.Event;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment implements EventDetailContract.View {

    private static final String ARGS_EVENT_NAME = "EVENT_NAME";

    private EventDetailContract.Presenter presenter;

    private EditText eventNameView;

    private EditText eventStartView;

    private EditText eventDueView;

    private EditText eventPriorityView;

    private Switch repeatSwitch;

    private Switch alarmSwitch;

    private CheckBox completeCheckBox;

    private final static String TAG="EVENT_DETAIL";


    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment newInstance(@Nullable String eventName) {
        Bundle args = new Bundle();
        args.putString(ARGS_EVENT_NAME, eventName);
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);
        eventNameView = root.findViewById(R.id.event_detail_name_edittext);
        eventPriorityView = root.findViewById(R.id.event_detail_priority_edittext);
        eventStartView = root.findViewById(R.id.event_detail_start_date_edittext);
        eventDueView = root.findViewById(R.id.event_detail_due_date_edittext);
        repeatSwitch = root.findViewById(R.id.event_detail_repeat_switch);
        alarmSwitch = root.findViewById(R.id.event_detail_alarm_switch);
        completeCheckBox = root.findViewById(R.id.event_detail_complete_switch);
        Log.d("EVENT_DETAIL", "EVENT_DETAIL_FRAG: called onCreateView");

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.event_detail_menu, menu);
    }

    //在此处完成删除、返回、保存操作
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_detail_delete:
                showConfirmDeleteDialog();
                Log.d("EVENT_DETAIL", "onOptionsItemSelected: delete");
                return true;
            case R.id.event_detail_save:
                presenter.saveCurrent();
                Log.d("EVENT_DETAIL", "onOptionsItemSelected: save");
                return true;
            default:
                getActivity().onBackPressed();
                Log.d("EVENT_DETAIL", "onOptionsItemSelected: back");
                return true;
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        //updateData();
        super.onDestroy();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning!");
        builder.setMessage("Would you really want to delete this event?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!presenter.isNewEvent()) {
                    presenter.deleteEvent();
                }else {
                    getActivity().finish();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void setPresenter(EventDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setEventInfo(Event event) {
        eventNameView.setText(event.getEventName());
        eventPriorityView.setText(getEventPriority(event.getEventPriority()));
        if (event.getEventStartDate() == null || event.getEventStartDate().getTime() == 0) {
            eventStartView.setHint("null");
        } else {
            eventStartView.setText(event.getEventStartDate().toString());
        }
        if (event.getEventDuetDate() == null || event.getEventDuetDate().getTime() == 0) {
            eventDueView.setHint("null");
        } else {
            eventDueView.setText(event.getEventDuetDate().toString());
        }
        repeatSwitch.setChecked(event.isEventRepeat());
        alarmSwitch.setChecked(event.isEventNeedAlarm());
        completeCheckBox.setChecked(event.isEventCompleted());
    }

    private String getEventPriority(int eventPriority) {
        switch (eventPriority) {
            case Event.PRIORITY_LOW:
                return "Low";
            case Event.PRIORITY_MEDIUM:
                return "Medium";
            case Event.PRIORITY_HIGH:
                return "High";
            default:
                return "Default";
        }
    }

    private int getEventPriority(@Nullable String eventPriority) {
        switch (eventPriority) {
            case "High":
                return Event.PRIORITY_HIGH;
            case "Medium":
                return Event.PRIORITY_MEDIUM;
            case "Low":
                return Event.PRIORITY_LOW;
            default:
                return -1;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showEventsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void updateData() {
        Event tempEvent = getCurrent();
        if (tempEvent != null) {
            presenter.saveEvent(tempEvent);
        } else {
            showEmptyEventError();
        }
    }

    @Override
    public Event getCurrent() {
        String name = eventNameView.getText().toString();

        if (name.isEmpty()){
            //showEmptyNameError();
            return null;
        }

        String startDateStr = eventStartView.getText().toString();
        String dueDateStr = eventDueView.getText().toString();
        Date start = null;
        Date due = null;
        try {
            if (startDateStr.isEmpty()) {
                start = new Date(0);
            } else {
                start = Date.valueOf(startDateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dueDateStr.isEmpty()) {
                due = new Date(0);
            } else {
                due = Date.valueOf(dueDateStr);
            }
            Log.d("EVENT_DETAIL", "updateData: " + due.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String priorityStr = eventPriorityView.getText().toString();

        int priority = -1;

        try{
            if(!priorityStr.isEmpty()){
                priority=getEventPriority(priorityStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        boolean repeat = repeatSwitch.isChecked();
        boolean alarm = alarmSwitch.isChecked();
        boolean completed = completeCheckBox.isChecked();
        Log.d(TAG, "getCurrent: "+priority);

        return new Event(name, start, due, completed, repeat, alarm, priority, null);
    }

    @Override
    public void showEmptyEventError() {
        Snackbar snackbar = Snackbar.make(requireView(), R.string.event_empty_error_message, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }



    @Override
    public void showEmptyNameError() {
        Snackbar snackbar = Snackbar.make(requireView(), R.string.event_empty_name_error_message, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void showVariableEmptyError() {
        Snackbar snackbar = Snackbar.make(requireView(), R.string.event_empty_variable_error_message, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void showNothingChanged() {
        Snackbar snackbar = Snackbar.make(requireView(), R.string.event_nothing_changed_message, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}