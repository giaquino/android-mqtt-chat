package com.giaquino.chat.model;

import android.support.annotation.NonNull;
import com.giaquino.chat.model.db.Database;
import com.giaquino.chat.model.entity.Profile;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import java.util.concurrent.Executor;
import rx.Observable;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
public class ProfileModel {

    private Database database;
    private Executor executor;
    private QueryObservable profile;

    public ProfileModel(@NonNull Database database, @NonNull Executor executor) {
        this.database = database;
        this.executor = executor;
        this.profile = this.database.query(Profile.TABLE_NAME, Profile.SELECT_PROFILE, null);
    }

    @NonNull public Observable<Profile> profile() {
        return profile.map(SqlBrite.Query::run).map(cursor -> {
            Timber.d("Profile queried: %s", cursor.getCount());
            if (cursor.moveToNext()) {
                return Profile.MAPPER.map(cursor);
            }
            return null;
        }).filter(p -> p != null);
    }

    public void setProfile(@NonNull String facebookId, @NonNull String firstName,
        @NonNull String lastName, @NonNull String picture) {
        executor.execute(() -> database.insert(Profile.TABLE_NAME,
            new Profile.Marshal().facebookId(facebookId)
                .name(firstName + " " + lastName)
                .picture(picture)
                .asContentValues()));
    }
}
