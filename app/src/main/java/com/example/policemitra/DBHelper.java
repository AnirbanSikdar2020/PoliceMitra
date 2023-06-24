package com.example.policemitra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "UserDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserDB (name TEXT,email TEXT primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists UserDB");
    }

    public Boolean insertUserData(String name,String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("email",email);
        long result = DB.insert("UserDB",null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateUserData(String name,String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        Cursor cursor = DB.rawQuery("Select * from UserDB where email=?", new String[]{email});
        if(cursor.getCount()>0)
        {
            long result = DB.update("UserDB",contentValues,"email=?",new String[]{email});
            if(result==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }
        else
        {
            return false;
        }

    }

    public Boolean deleteUserData(String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDB where email=?", new String[]{email});
        if(cursor.getCount()>0)
        {
            long result = DB.delete("UserDB","email=?",new String[]{email});
            if(result==-1)
            {
                return false;
            }
            else {
                return true;
            }
        }
        else
        {
            return false;
        }

    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from UserDB", null);
        return cursor;

    }
}
