package com.giaquino.chat.model.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import java.util.List;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
public class SqlBriteDatabase implements Database {

    @NonNull private BriteDatabase db;

    public SqlBriteDatabase(@NonNull SQLiteOpenHelper helper, boolean debug) {
        db = SqlBrite.create(message -> Timber.d(message))
            .wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(debug);
    }

    @NonNull @Override public QueryObservable query(@NonNull String table, @NonNull String query,
        String[] selectionArgs) {
        return db.createQuery(table, query, selectionArgs);
    }

    @Override public long insert(@NonNull String table, @NonNull ContentValues contentValues) {
        return db.insert(table, contentValues);
    }

    @Override public int insert(@NonNull String table, @NonNull List<ContentValues> contentValues) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ContentValues cv : contentValues) {
                db.insert(table, cv);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return contentValues.size();
    }

    @Override public int update(@NonNull String table, @NonNull ContentValues contentValues,
        @NonNull String where, @NonNull String[] whereArgs) {
        return db.update(table, contentValues, where, whereArgs);
    }

    @Override public int delete(@NonNull String table, String where, String[] whereArgs) {
        return db.delete(table, where, whereArgs);
    }
}
