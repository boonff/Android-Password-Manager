package com.example.passwords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.passwords.database.DatabaseHelper;
import com.example.passwords.database.Password;

import java.util.ArrayList;
import java.util.List;

public class PasswordRepository {
    private DatabaseHelper dbHelper;

    public PasswordRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertPassword(String name, String username, String password, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_URL, url);
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public List<Password> getAllPasswords() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                null, null, null, null, null, null
        );

        List<Password> passwords = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URL));
            passwords.add(new Password(id, name, username, password, url));
        }
        cursor.close();
        return passwords;
    }
}
