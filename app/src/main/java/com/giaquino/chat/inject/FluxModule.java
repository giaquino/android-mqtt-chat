package com.giaquino.chat.inject;

import com.giaquino.chat.flux.action.ConnectionActionCreator;
import com.giaquino.chat.flux.action.MessageActionCreator;
import com.giaquino.chat.flux.action.ProfileActionCreator;
import com.giaquino.chat.flux.action.SubscriptionActionCreator;
import com.giaquino.chat.flux.dispatcher.Dispatcher;
import com.giaquino.chat.flux.store.ConnectionStore;
import com.giaquino.chat.flux.store.MessageStore;
import com.giaquino.chat.flux.store.ProfileStore;
import com.giaquino.chat.flux.store.SubscriptionStore;
import com.giaquino.chat.model.ConnectionModel;
import com.giaquino.chat.model.MessageModel;
import com.giaquino.chat.model.ProfileModel;
import com.giaquino.chat.model.SubscriptionModel;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/18/16
 */
@Module public class FluxModule {

    @Provides @Singleton public Dispatcher provideDispatcher(ConnectionStore connectionStore,
        SubscriptionStore subscriptionStore, MessageStore messageStore, ProfileStore profileStore) {
        return new Dispatcher(
            Arrays.asList(connectionStore, subscriptionStore, messageStore, profileStore));
    }

    @Provides @Singleton public ConnectionStore provideConnectionStore() {
        return new ConnectionStore();
    }

    @Provides @Singleton public SubscriptionStore provideSubscriptionStore() {
        return new SubscriptionStore();
    }

    @Provides @Singleton public MessageStore provideMessageStore() {
        return new MessageStore();
    }

    @Provides @Singleton public ProfileStore provideProfileStore() {
        return new ProfileStore();
    }

    @Provides @Singleton
    public ConnectionActionCreator provideConnectionActionCreator(Dispatcher dispatcher,
        ConnectionModel connectionModel) {
        return new ConnectionActionCreator(dispatcher, connectionModel);
    }

    @Provides @Singleton
    public SubscriptionActionCreator provideSubscriptionActionCreator(Dispatcher dispatcher,
        SubscriptionModel subscriptionModel) {
        return new SubscriptionActionCreator(dispatcher, subscriptionModel);
    }

    @Provides @Singleton
    public MessageActionCreator provideMessageActionCreator(Dispatcher dispatcher,
        MessageModel messageModel) {
        return new MessageActionCreator(dispatcher, messageModel);
    }

    @Provides @Singleton
    public ProfileActionCreator provideProfileActionCreator(Dispatcher dispatcher,
        ProfileModel profileModel) {
        return new ProfileActionCreator(dispatcher, profileModel);
    }
}
