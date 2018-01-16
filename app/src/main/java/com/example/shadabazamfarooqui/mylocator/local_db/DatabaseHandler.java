package com.example.shadabazamfarooqui.mylocator.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;


import com.example.shadabazamfarooqui.mylocator.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHADAB AAZAM on 22-07-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "AutoLogin";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "password";

    public Cursor cursor;

    public DatabaseHandler(Context context) {
        super(context, "mydb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + NAME + " TEXT," + EMAIL + " TEXT," + MOBILE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long insertRecord(UserBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, bean.getName());
        values.put(EMAIL, bean.getEmail());
        values.put(MOBILE, bean.getMobile());
        return db.insert(TABLE_NAME, null, values);
    }

    public boolean updateStudent(UserBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOBILE, bean.getPassword());
        return db.update(TABLE_NAME, contentValues, MOBILE + "=" + bean.getMobile(), null) > 0;
    }


    public Cursor getRecord() {
        // Select single student Query
        String selectQuery = " select " + NAME + " , " + EMAIL + "," + MOBILE + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(NAME));
            cursor.getString(cursor.getColumnIndex(EMAIL));
            cursor.getString(cursor.getColumnIndex(MOBILE));
        }
        return cursor;
    }


    public boolean isAlreadyLoggedIn() {
        // Select single student Query
        String selectQuery = " select " + MOBILE + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }


    public String getMob() {
        // Select single student Query
        String mobile = null;
        String selectQuery = "SELECT  " + MOBILE + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            mobile = cursor.getString(cursor.getColumnIndex(MOBILE));
        }

        return mobile;
    }

    public void deleteTable() {
        String selectQuery = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);
        db.execSQL("delete from " + TABLE_NAME);
        db.close();


    }


}
