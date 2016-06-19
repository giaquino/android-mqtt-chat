package com.giaquino.chat.flux.store;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.action.Action;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public abstract class Store<T extends Store> {

    private BehaviorSubject<T> publisher = BehaviorSubject.create();

    protected void notifyStoreChanged(@NonNull T store) {
        publisher.onNext(store);
    }

    @NonNull public Observable<T> asObservable() {
        return publisher.asObservable().subscribeOn(AndroidSchedulers.mainThread());
    }

    public abstract void dispatchAction(@NonNull Action action);
}
