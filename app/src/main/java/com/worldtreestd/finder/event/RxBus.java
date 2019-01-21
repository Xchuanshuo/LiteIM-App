package com.worldtreestd.finder.event;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author Legend
 * @data by on 19-1-20.
 * @description
 */
public class RxBus {

    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        this.mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault() {
        return Holder.INSTANCE;
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    public boolean hasObservers() {
        return mBus.hasSubscribers();
    }

    public static class Holder {
        private static final RxBus INSTANCE = new RxBus();
    }
}
