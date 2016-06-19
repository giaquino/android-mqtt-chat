package com.giaquino.chat.inject;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giaquino.chat.BuildConfig;
import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.model.ConnectionModel;
import com.giaquino.chat.model.MessageModel;
import com.giaquino.chat.model.ProfileModel;
import com.giaquino.chat.model.SubscriptionModel;
import com.giaquino.chat.model.db.ChatOpenHelper;
import com.giaquino.chat.model.db.Database;
import com.giaquino.chat.model.db.SqlBriteDatabase;
import com.giaquino.chat.model.image.ImageLoader;
import com.giaquino.chat.model.image.PicassoImageLoader;
import com.giaquino.chat.model.mqtt.MqttManager;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/10/16
 */
@Module public class ModelModule {

    private String databaseName;
    private int databaseVersion;

    public ModelModule(@NonNull String databaseName, @IntRange(from = 1) int databaseVersion) {
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    @Provides @Singleton public ImageLoader provideImageLoader(ChatApplication application,
        @NonNull OkHttpClient client) {
        Picasso picasso = new Picasso.Builder(application).downloader(new OkHttp3Downloader(client))
            .build();
        return new PicassoImageLoader(picasso);
    }

    @Provides @Singleton public ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    @Provides @Singleton public Database provideDatabase(ChatApplication application) {
        return new SqlBriteDatabase(
            new ChatOpenHelper(application, databaseName, null, databaseVersion),
            BuildConfig.DEBUG);
    }

    @Provides @Singleton public ConnectionModel provideConnectionModel(MqttManager mqttManager) {
        return new ConnectionModel(mqttManager);
    }

    @Provides @Singleton
    public SubscriptionModel provideSubscriptionModel(MqttManager mqttManager) {
        return new SubscriptionModel(mqttManager);
    }

    @Provides @Singleton
    public MessageModel provideMessageModel(MqttManager mqttManager, Executor executor) {
        return new MessageModel(mqttManager, executor);
    }

    @Provides @Singleton
    public ProfileModel provideProfileModel(Database database, Executor executor) {
        return new ProfileModel(database, executor);
    }
}
