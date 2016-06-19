package com.giaquino.chat.inject;

import android.support.annotation.NonNull;
import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.common.concurrent.BackgroundExecutor;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/11/16
 */
@Module public class ApplicationModule {

    private final ChatApplication application;

    public ApplicationModule(@NonNull ChatApplication application) {
        this.application = application;
    }

    @Provides @Singleton public ChatApplication provideApplication() {
        return application;
    }

    @Provides @Singleton public Executor provideBackgroundExecutor() {
        return new BackgroundExecutor();
    }
}
