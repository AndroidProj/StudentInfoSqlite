package com.example.vasundhara.studentinfosqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Vasundhara on 9/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "Students.db";
    private static final String TABLE_NAME = "student";
    public static final String ID = "id";
    public static final String FNAME = "firstname";
    public static final String LNAME = "lastname";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FNAME + " TEXT," + LNAME + " TEXT," + MOBILE + " INTEGER(10)," + EMAIL + " VARCHAR(320))";
        db.execSQL(query);
        Log.d(TAG, "students Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean insertData(String first, String last, long number, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FNAME, first);
        cv.put(LNAME, last);
        cv.put(MOBILE, number);
        cv.put(EMAIL, email);

        long res = db.insert(TABLE_NAME, null, cv);
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT rowid _id, *FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_NAME, ID + "='" + id + "'", null);
        Log.e(TAG, "delete result" + res);
        if (res == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateData(String id, String first, String last, long number, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FNAME, first);
        cv.put(LNAME, last);
        cv.put(MOBILE, number);
        cv.put(EMAIL, email);
        int res = db.update(TABLE_NAME, cv, ID + "=" + id, null);
        Log.e(TAG, "Update Result : " + res);
        if (res == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor fetchByFirstName(String inputText) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            String query = "SELECT rowid _id, *FROM " + TABLE_NAME;
            mCursor = db.rawQuery(query, null);
            return mCursor;

        } else {
            mCursor = db.rawQuery("SELECT rowid _id, *FROM " + TABLE_NAME + " WHERE " + FNAME + " LIKE '" + inputText + "%'", null);
//            mCursor = db.query(true, TABLE_NAME, new String[]{ID, FNAME,
//                            LNAME, MOBILE, EMAIL},
//                    FNAME + " like '%" + inputText + "%'", null,
//                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
