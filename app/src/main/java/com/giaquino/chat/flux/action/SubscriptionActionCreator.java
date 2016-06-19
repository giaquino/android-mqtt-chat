package com.giaquino.chat.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.dispatcher.Dispatcher;
import com.giaquino.chat.flux.store.SubscriptionStore;
import com.giaquino.chat.model.SubscriptionModel;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class SubscriptionActionCreator {

    private SubscriptionModel subscriptionModel;

    public SubscriptionActionCreator(@NonNull Dispatcher dispatcher,
        @NonNull SubscriptionModel model) {
        subscriptionModel = model;
        subscriptionModel.subscriptions().subscribe(s -> {
            dispatcher.dispatch(Action.create(SubscriptionStore.ACTION_ADD_SUBSCRIPTION, s));
        });
        subscriptionModel.unsubscriptions().subscribe(s -> {
            dispatcher.dispatch(Action.create(SubscriptionStore.ACTION_REMOVE_SUBSCRIPTION, s));
        });
        subscriptionModel.subscriptionsError().subscribe(s -> {
            dispatcher.dispatch(Action.create(SubscriptionStore.ACTION_FAILED_SUBSCRIPTION, s));
        });
        subscriptionModel.unsubscriptionsError().subscribe(s -> {
            dispatcher.dispatch(Action.create(SubscriptionStore.ACTION_FAILED_UNSUBSCRIPTION, s));
        });
    }

    public void subscribe(@NonNull String topic) {
        subscriptionModel.subscribe(topic);
    }

    public void unsubscribe(@NonNull String topic) {
        subscriptionModel.unsubscribe(topic);
    }
}
