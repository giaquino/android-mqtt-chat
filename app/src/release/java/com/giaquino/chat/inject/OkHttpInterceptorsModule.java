package com.giaquino.chat.inject;

import dagger.Module;
import dagger.Provides;
import java.util.Collections;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.Interceptor;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/11/16
 */
@Module public class OkHttpInterceptorsModule {

    public static final String INTERCEPTOR = "interceptor";
    public static final String NETWORK_INTERCEPTOR = "network_interceptor";

    @Provides @Singleton @Named(INTERCEPTOR) public List<Interceptor> provideOkHttpInterceptor() {
        return Collections.emptyList();
    }

    @Provides @Singleton @Named(NETWORK_INTERCEPTOR)
    public List<Interceptor> provideOkHttpNetworkInterceptor() {
        return Collections.emptyList();
    }
}
