package com.worldtreestd.finder.event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Legend
 * @data by on 19-1-20.
 * @description
 */
public class RxBus {

    private final Subject<Object> mBus;

    private RxBus() {
        this.mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getDefault() {
        return Holder.INSTANCE;
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public static class Holder {
        private static final RxBus INSTANCE = new RxBus();
    }
}
