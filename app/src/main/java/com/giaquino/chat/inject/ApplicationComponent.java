package com.giaquino.chat.inject;

import android.support.annotation.NonNull;
import com.giaquino.chat.ui.chat.ChatActivity;
import com.giaquino.chat.ui.login.LoginActivity;
import dagger.Component;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/11/16
 */
@Singleton @Component(modules = {
    ApplicationModule.class, FluxModule.class, ModelModule.class, MqttModule.class,
    NetworkModule.class, OkHttpInterceptorsModule.class
}) public interface ApplicationComponent {

    void inject(@NonNull LoginActivity activity);

    void inject(@NonNull ChatActivity activity);
}
