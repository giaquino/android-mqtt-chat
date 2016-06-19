package com.giaquino.chat.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.giaquino.chat.model.entity.Profile;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
public class ChatOpenHelper extends SQLiteOpenHelper {

    public ChatOpenHelper(@NonNull Context context, @NonNull String name,
        @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(Profile.CREATE_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Profile.DROP_TABLE);
        onCreate(db);
    }
}
