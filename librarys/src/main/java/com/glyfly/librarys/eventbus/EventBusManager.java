package com.glyfly.librarys.eventbus;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/10/26.
 */

public class EventBusManager {
    private static final EventBus BUS = new EventBus();

    private EventBusManager(){}

    public static EventBus getInstance(){
        return BUS;
    }
}
