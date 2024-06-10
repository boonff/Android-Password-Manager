package com.example.passwords.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.passwords.key.EncryptionUtil;
import com.example.passwords.key.KeyStoreUtil;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class PasswordRepository {
    private DatabaseHelper dbHelper;

    public PasswordRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
        try {
            KeyStoreUtil.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertPassword(String name, String username, String password, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        try {
            SecretKey key = KeyStoreUtil.getKey();
            String encryptedPassword = EncryptionUtil.encrypt(password, key);
            values.put(DatabaseHelper.COLUMN_PASSWORD, encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            SecretKey key = KeyStoreUtil.getKey();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                String encryptedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URL));
                String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword, key);
                passwords.add(new Password(id, name, username, decryptedPassword, url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return passwords;
    }

    public List<Password> findPasswordByName(String findName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COLUMN_NAME + " LIKE ? OR " + DatabaseHelper.COLUMN_USERNAME + " LIKE ?"+ DatabaseHelper.COLUMN_URL + " LIKE ?";


        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                null,
                selection,
                new String[]{"%" + findName + "%", "%" + findName + "%", "%" + findName + "%"},
                null, null, null
        );

        List<Password> passwords = new ArrayList<>();
        try {
            SecretKey key = KeyStoreUtil.getKey();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                String encryptedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URL));
                String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword, key);
                passwords.add(new Password(id, name, username, decryptedPassword, url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return passwords;
    }

    public void updatePasswordById(int id, String name, String username, String password, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        try {
            SecretKey key = KeyStoreUtil.getKey();
            String encryptedPassword = EncryptionUtil.encrypt(password, key);
            values.put(DatabaseHelper.COLUMN_PASSWORD, encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return; // 如果加密失败，直接返回
        }
        values.put(DatabaseHelper.COLUMN_URL, url);

        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try {
            int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0) {
                System.out.println("No rows updated, check the id.");
            } else {
                System.out.println("Update successful.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // 确保数据库连接在操作后关闭
        }
    }

    public void deletePassword(Password password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(password.getId())};

        try {
            int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                System.out.println("No rows deleted, check the id.");
            } else {
                System.out.println("Delete successful.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // 确保数据库连接在操作后关闭
        }
    }

    public void clearAllPasswords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, null, null);
        db.close();
    }
}
