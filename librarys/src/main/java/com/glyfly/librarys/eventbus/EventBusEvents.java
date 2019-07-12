package com.glyfly.librarys.eventbus;

/**
 * Created by Administrator on 2017/10/26.
 */

public class EventBusEvents {

    public static class CheckVersionEvent{
        public int ret;
        public Object strategy;
        public boolean isManual;
        public boolean isSilence;
    }
}
