package com.giaquino.chat.model;

import android.support.annotation.NonNull;
import com.giaquino.chat.model.mqtt.MqttManager;
import com.giaquino.chat.model.mqtt.MqttSubscriptionListener;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public class SubscriptionModel {

    private MqttManager mqttManager;
    private PublishSubject<String> subscriptions = PublishSubject.create();
    private PublishSubject<String> subscriptionsError = PublishSubject.create();
    private PublishSubject<String> unsubscriptions = PublishSubject.create();
    private PublishSubject<String> unsubscriptionsError = PublishSubject.create();

    public SubscriptionModel(@NonNull MqttManager manager) {
        mqttManager = manager;
        mqttManager.setMqttSubscriptionListener(new MqttSubscriptionListener() {
            @Override public void onSubscriptionSuccess(String topic, int qos) {
                Timber.d("Subscription success: %s", topic);
                subscriptions.onNext(topic);
            }

            @Override public void onSubscriptionFailure(String topic, int qos) {
                Timber.d("Subscription failure: %s", topic);
                subscriptionsError.onNext(topic);
            }

            @Override public void onUnsubscriptionSuccess(String topic) {
                Timber.d("Unsubscription success: %s", topic);
                unsubscriptions.onNext(topic);
            }

            @Override public void onUnsubscriptionFailure(String topic) {
                Timber.d("Unsubscription failure: %s", topic);
                unsubscriptionsError.onNext(topic);
            }
        });
    }

    public void subscribe(@NonNull String topic) {
        mqttManager.subscribe(topic, computeQosBaseOnConnectivity());
    }

    public void unsubscribe(@NonNull String topic) {
        mqttManager.unsubscribe(topic);
    }

    @NonNull public Observable<String> subscriptions() {
        return subscriptions.asObservable();
    }

    @NonNull public Observable<String> subscriptionsError() {
        return subscriptionsError.asObservable();
    }

    @NonNull public Observable<String> unsubscriptions() {
        return unsubscriptions.asObservable();
    }

    @NonNull public Observable<String> unsubscriptionsError() {
        return unsubscriptionsError.asObservable();
    }

    private int computeQosBaseOnConnectivity() {
        return 1;
    }
}
