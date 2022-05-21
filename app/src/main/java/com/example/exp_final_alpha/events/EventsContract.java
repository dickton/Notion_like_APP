package com.example.exp_final_alpha.events;

import androidx.annotation.NonNull;

import com.example.exp_final_alpha.BasePresenter;
import com.example.exp_final_alpha.BaseView;
import com.example.exp_final_alpha.data.Event;

import java.util.List;

public interface EventsContract {

    interface View extends BaseView<Presenter>{



        void showAddEvent();

        void showEventDetailsUi(@NonNull String eventName);

        //void showInProgressEvents();

        //void showNextUpEvents();

        //void showOverdueEvents();

        //void showRepeatEvents();

        //void showOverdueEventsCleared();

        void showLoadingEventsError();

        void showNoEvents();

        //void showAddEventsTag();

        boolean isActive();

        void addEventListsToAdapter(List<Event>[] lists);


        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadEvents(boolean forceUpdate);

        void sortEvents(List<Event> events);

        void addNewEvent();

        void openEventDetails(@NonNull String eventName);

        void clearOverdueEvents();

        void notifyRestart();

    }
}
