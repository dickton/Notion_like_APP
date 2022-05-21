package com.example.exp_final_alpha.events;

import androidx.annotation.NonNull;

import com.example.exp_final_alpha.BasePresenter;
import com.example.exp_final_alpha.BaseView;
import com.example.exp_final_alpha.data.Event;

import java.util.List;

public interface EventsContract {

    interface View extends BaseView<Presenter>{

        void showAddEvent();        //展示添加事项页面

        void showEventDetailsUi(@NonNull String eventName);     //展示到详情页面

        void showLoadingEventsError();      //显示从数据库中加载事件时出错

        boolean isActive();     //返回当前Fragment是否显示到Activity上

        void addEventListsToAdapter(List<Event>[] lists);//将数据库返回的列表数组交给RecyclerView的Adapter

        void showSuccessfullySavedMessage();

        void showNoEvents();

        //void showInProgressEvents();

        //void showNextUpEvents();

        //void showOverdueEvents();

        //void showRepeatEvents();

        //void showOverdueEventsCleared();

        //void showAddEventsTag();
    }

    interface Presenter extends BasePresenter {

        void loadEvents(boolean forceUpdate);   //从数据库读取事件

        void sortEvents(List<Event> events);    //将读取到的事件分类

        void addNewEvent();     //添加新事件

        void openEventDetails(@NonNull String eventName);   //展示事件详情

        void notifyRestart();   //通知Fragment中的RecyclerView数据可能更新了

        //void result(int requestCode, int resultCode);

        void clearOverdueEvents();

    }
}
