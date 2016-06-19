package com.giaquino.chat;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.facebook.FacebookSdk;
import com.giaquino.chat.inject.ApplicationComponent;
import com.giaquino.chat.inject.ApplicationModule;
import com.giaquino.chat.inject.DaggerApplicationComponent;
import com.giaquino.chat.inject.ModelModule;
import com.giaquino.chat.inject.MqttModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/10/16
 */
public class ChatApplication extends Application {

    private RefWatcher refWatcher;
    private ApplicationComponent applicationComponent;

    public static ChatApplication get(@NonNull Context context) {
        return (ChatApplication) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .mqttModule(new MqttModule("tcp://broker.hivemq.com:1883"))
            .modelModule(new ModelModule("chat.db", 1))
            .build();
        refWatcher = LeakCanary.install(this);
        LeakCanary.enableDisplayLeakActivity(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public ApplicationComponent applicationComponent() {
        return applicationComponent;
    }

    public RefWatcher refWatcher() {
        return refWatcher;
    }
}
