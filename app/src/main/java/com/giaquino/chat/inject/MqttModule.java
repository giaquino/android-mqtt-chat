package com.giaquino.chat.inject;

import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.model.mqtt.MqttManager;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
@Module public class MqttModule {

    private String url;

    public MqttModule(String url) {
        this.url = url;
    }

    @Provides @Singleton
    public MqttManager provideMqttManager(ChatApplication application, Executor executor) {
        return new MqttManager(application, url, executor);
    }
}
