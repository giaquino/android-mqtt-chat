package com.giaquino.chat.model.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.squareup.sqlbrite.QueryObservable;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
public interface Database {

    @NonNull QueryObservable query(@NonNull String table, @NonNull String query,
        @Nullable String[] selectionArgs);

    long insert(@NonNull String table, @NonNull ContentValues contentValues);

    int insert(@NonNull String table, @NonNull List<ContentValues> contentValues);

    int update(@NonNull String table, @NonNull ContentValues contentValues, @NonNull String where,
        @NonNull String[] whereArgs);

    int delete(@NonNull String table, @Nullable String where, @Nullable String[] whereArgs);
}
