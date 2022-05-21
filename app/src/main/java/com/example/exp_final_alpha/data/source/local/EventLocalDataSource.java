package com.example.exp_final_alpha.data.source.local;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;

import com.example.exp_final_alpha.R;
import com.example.exp_final_alpha.data.Event;
import com.example.exp_final_alpha.data.source.EventDataSource;
import com.example.exp_final_alpha.util.AppExecutors;

import java.util.List;

public class EventLocalDataSource implements EventDataSource {

    private  static volatile EventLocalDataSource INSTANCE;
    private EventDao eventDao;
    private AppExecutors appExecutors;

    private final static int NO_STATUS=0;
    private final static int NEXT_UP=1;
    private final static int IN_PROGRESS=2;
    private final static int LONG_TERM=3;
    private final static int DONE_OR_OVERDUE=4;


    public EventLocalDataSource(AppExecutors appExecutors, EventDao eventDao) {
        this.eventDao = eventDao;
        this.appExecutors = appExecutors;
    }

    public static EventLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull EventDao eventDao){
        if(INSTANCE==null){
            synchronized (EventLocalDataSource.class){
                if(INSTANCE==null){
                    INSTANCE=new EventLocalDataSource(appExecutors,eventDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getEvents(@NonNull final LoadEventsCallback callback) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                final List<Event> events=eventDao.getEvents();
                appExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(events.isEmpty()){
                            callback.onDataNotAvailable();
                        }else {
                            callback.onEventsLoaded(events);
                        }
                    }
                });
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void getEvent(@NonNull String eventName, @NonNull GetEventCallback callback) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                final Event event=eventDao.getEventsByName(eventName);
                appExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(event==null){
                            callback.onDataNotAvailable();
                        } else{
                            callback.onEventLoaded(event);
                        }
                    }
                });
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void getEvents(@NonNull int eventType, @NonNull LoadEventsCallback callback) {
        switch (eventType){
            case NO_STATUS:
                Runnable runnable1=new Runnable() {
                    @Override
                    public void run() {
                        final List<Event> events=eventDao.getNoStatusEvents();
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(events.isEmpty()){
                                    callback.onDataNotAvailable();
                                }else {
                                    callback.onEventsLoaded(events);
                                }
                            }
                        });
                    }
                };
                appExecutors.getDiskIO().execute(runnable1);
                break;
            case NEXT_UP:
                Runnable runnable2=new Runnable() {
                    @Override
                    public void run() {
                        final List<Event> events=eventDao
                                .getNextUpEvents(System.currentTimeMillis());
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(events.isEmpty()){
                                    callback.onDataNotAvailable();
                                }else {
                                    callback.onEventsLoaded(events);
                                }
                            }
                        });
                    }
                };
                appExecutors.getDiskIO().execute(runnable2);
                break;
            case IN_PROGRESS:
                Runnable runnable3=new Runnable() {
                    @Override
                    public void run() {
                        final List<Event> events=eventDao
                                .getInProgressEvents(System.currentTimeMillis());
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(events.isEmpty()){
                                    callback.onDataNotAvailable();
                                }else {
                                    callback.onEventsLoaded(events);
                                }
                            }
                        });
                    }
                };
                appExecutors.getDiskIO().execute(runnable3);
                break;
            case LONG_TERM:
                Runnable runnable4=new Runnable() {
                    @Override
                    public void run() {
                        final List<Event> events=eventDao.getRepeatEvents(System.currentTimeMillis());
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(events.isEmpty()){
                                    callback.onDataNotAvailable();
                                }else {
                                    callback.onEventsLoaded(events);
                                }
                            }
                        });
                    }
                };
                appExecutors.getDiskIO().execute(runnable4);
                break;
            case DONE_OR_OVERDUE:
                Runnable runnable5=new Runnable() {
                    @Override
                    public void run() {
                        final List<Event> events=eventDao
                                .getCompletedOrOverdueEvents(System.currentTimeMillis());
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(events.isEmpty()){
                                    callback.onDataNotAvailable();
                                }else {
                                    callback.onEventsLoaded(events);
                                }
                            }
                        });
                    }
                };
                appExecutors.getDiskIO().execute(runnable5);
            default:
                break;
        }

    }


    @Override
    public void saveEvent(@NonNull Event event) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.insertEvent(event);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void completeEvent(@NonNull String eventName) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.updateCompleted(eventName,true);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void activateEvent(@NonNull String eventName) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.updateCompleted(eventName,false);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void enableRepeatEvent(@NonNull String eventName) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.updateRepeat(eventName,true);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }


    @Override
    public void cancelRepeatEvent(@NonNull String eventName) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.updateRepeat(eventName,false);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void deleteEvent(@NonNull String eventName) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                eventDao.deleteEventByEventName(eventName);
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }
}
