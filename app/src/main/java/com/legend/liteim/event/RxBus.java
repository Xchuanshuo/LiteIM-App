package com.legend.liteim.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Legend
 * @data by on 19-1-20.
 * @description 事件总线 支持粘性事件
 */
public class RxBus {

    private final Subject<Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;

    private RxBus() {
        this.mBus = PublishSubject.create().toSerialized();
        this.mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        return Holder.INSTANCE;
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    public void postSticky(Object event) {
        synchronized(mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }


    public <T> Observable<T> toObservableSticky(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                // 合并后保证后续注册的订阅者也能消费该事件
                return observable.mergeWith(Observable.create(emitter -> emitter.onNext(eventType.cast(event))));
            } else {
                return observable;
            }
        }
    }

    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    public void removeAllStickyEvent() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }


    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public static class Holder {
        private static final RxBus INSTANCE = new RxBus();
    }
}
