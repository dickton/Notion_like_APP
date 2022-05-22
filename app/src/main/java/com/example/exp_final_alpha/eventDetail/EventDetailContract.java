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

        void showEventsList();      //返回到事件列表界面

        void showEmptyEventError(); //当读取出错时展示

        Event getCurrent();     //读取当前Fragment上的信息，并返回一个Event对象

        void showEmptyNameError();     //当表单缺少事件名称时展示Snackbar

        void showVariableEmptyError();  //当表单缺少其他必填事项时展示Snackbar

        void showNothingChanged();  //保存时，若无变化发生，展示Snackbar

    }
    interface Presenter extends BasePresenter{
        void saveEvent(Event event);    //调用数据库管理类保存事件

        void populateEvent();   //根据创建Presenter时传入的eventName到数据库查询事件

        boolean changeOccur(Event event);   //监测表单上的事件信息相较原信息是否变化

        boolean isNewEvent();   //返回事件是否为新创建事件

        void deleteEvent();     //删除事件

        void saveCurrent();     //保存当前表单上的事件

        boolean isDataMissing();
    }
}
