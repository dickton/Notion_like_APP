package com.example.exp_final_alpha.eventDetail;

import com.example.exp_final_alpha.BasePresenter;
import com.example.exp_final_alpha.BaseView;
import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.events.EventsContract;

public interface EventDetailContract {
    interface View extends BaseView<Presenter>{
        //void setLoadingIndicator(boolean active);

        void setEventInfo(Event event);

        boolean isActive();     //Fragment是否被添加到Activity中

        void showEventsList();      //展示

        void showEmptyEventError();

        void updateData();

        Event getCurrent();

        void showEmptyNameError();

        void showVariableEmptyError();

        void showNothingChanged();


    }
    interface Presenter extends BasePresenter{
        void saveEvent(Event event);

        boolean isDataMissing();

        void populateEvent();

        boolean changeOccur(Event event);

        boolean isNewEvent();

        void deleteEvent();

        void saveCurrent();
    }
}
