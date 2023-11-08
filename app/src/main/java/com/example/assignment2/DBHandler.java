package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "assignTwo";
    private static final String TABLE_NAME = "locations";
    private static final String IDS_COL = "id";
    private static final String ADDRESSES_COL = "address";
    private static final String LATITUDE_COL = "latitude";

    private static final String LONGITUDE_COL = "longitude";

    private static final int  DB_VERSION = 14;
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + IDS_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESSES_COL + " TEXT,"
                + LATITUDE_COL + " TEXT,"
                + LONGITUDE_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public boolean addNewLocation(String address, String latitude, String longitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();


        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ADDRESSES_COL, address);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        // after adding all values we are passing
        // content values to our tables

        long changes = db.insert(TABLE_NAME, null, values);
        if(changes == -1) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // this method is called to check if the table exists already.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean removeLocation(String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "address = ?";
        String[] whereArgs = {address};

        int changes = db.delete(TABLE_NAME, whereClause, whereArgs);
        if(changes == 0) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public ArrayList<Location> readLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Location> locArrayList = new ArrayList<>();
                if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                locArrayList.add(new Location(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        db.close();
        cursor.close();

        return locArrayList;
    }



    public int getID(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = -1;
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + ADDRESSES_COL + " = ?";
        String[] selectionArgs = {address};
        Log.i("address", address);

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.getCount() > 0) {
            id = cursor.getInt(0);
        }
        db.close();
        cursor.close();
        return id;
    }

    public boolean updateLocation(String address, String newAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDRESSES_COL, newAddress);

        String whereClause = "address = ?";
        String[] whereArgs = {address};

        int changes = db.update(TABLE_NAME, values, whereClause, whereArgs);
        if(changes == 0) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public String getCoordinates(String address) {
        String latitude, longitude;
        String result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "address = ?";
        String[] columns = {LATITUDE_COL, LONGITUDE_COL};
        String[] selectionArgs = {address};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int latIndex = cursor.getColumnIndex(LATITUDE_COL);
                int longIndex = cursor.getColumnIndex(LONGITUDE_COL);
                latitude = cursor.getString(latIndex);
                longitude = cursor.getString(longIndex);
                // Use latitude and longitude values here
                result = latitude + " " + longitude;
            } else {
                // Address not found in the database
            }
            cursor.close();
            db.close();
        }

        return result;
    }
}

