package com.giaquino.chat.flux.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.giaquino.chat.flux.action.Action;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class SubscriptionStore extends Store<SubscriptionStore> {

    public static final String ACTION_ADD_SUBSCRIPTION = "SubscriptionStore.ADD_SUBSCRIPTION";
    public static final String ACTION_REMOVE_SUBSCRIPTION = "SubscriptionStore.REMOVE_SUBSCRIPTION";
    public static final String ACTION_FAILED_SUBSCRIPTION = "SubscriptionStore.FAILED_SUBSCRIPTION";
    public static final String ACTION_FAILED_UNSUBSCRIPTION = "SubscriptionStore.FAILED_UNSUBSCRIPTION";

    private Set<String> subscriptions = new HashSet<>(10);
    private String failedSubscription;
    private String failedUnsubscription;

    @Nullable public String failedSubscription() {
        return failedSubscription;
    }

    @Nullable public String failedUnsubscription() {
        return failedUnsubscription;
    }

    @NonNull public Set<String> subscriptions() {
        return subscriptions;
    }

    @Override public void dispatchAction(@NonNull Action action) {
        failedSubscription = "";
        failedUnsubscription = "";
        switch (action.type()) {
            case ACTION_ADD_SUBSCRIPTION:
                subscriptions.add((String) action.data());
                break;

            case ACTION_REMOVE_SUBSCRIPTION:
                String topic = (String) action.data();
                subscriptions.remove(topic);
                break;

            case ACTION_FAILED_SUBSCRIPTION:
                failedSubscription = (String) action.data();
                break;

            case ACTION_FAILED_UNSUBSCRIPTION:
                failedUnsubscription = (String) action.data();
                break;

            default:
                return;
        }
        notifyStoreChanged(this);
    }
}
