/*
Class: DBHandler.java
Date: Nov 8th 2023
Programmer: Sarah Wedge
Description: This class creates and manages a database storing location values. The values can be obtained as an array list.
Each entry can be updated or deleted based on its address. Also, entries can be added by specifying their address, latitude, and longitude.
 */

package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    //initialize database structure
    private static final String DB_NAME = "assignTwo";
    private static final String TABLE_NAME = "locations";
    private static final String IDS_COL = "id";
    private static final String ADDRESSES_COL = "address";
    private static final String LATITUDE_COL = "latitude";

    private static final String LONGITUDE_COL = "longitude";

    private static final int  DB_VERSION = 14;

    //constructor
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Create database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + IDS_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESSES_COL + " TEXT,"
                + LATITUDE_COL + " TEXT,"
                + LONGITUDE_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    //Add a location entry to the database
    public boolean addNewLocation(String address, String latitude, String longitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Pass values for address, latitude, and longitude
        values.put(ADDRESSES_COL, address);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        long changes = db.insert(TABLE_NAME, null, values); //insert values into the database table as a new entry
        if(changes == -1) { //insert returns the row id of the newly added row or -1 if no row was added
            db.close();
            return false; //adding location was not successful
        }
        db.close();
        return true; //adding location was successful
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // this method is called to check if the table exists already.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Delete an entry from the database based on its address
    public boolean removeLocation(String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "address = ?";
        String[] whereArgs = {address};

        int changes = db.delete(TABLE_NAME, whereClause, whereArgs); //delete the entry, returns number of entries deleted
        if(changes == 0) { //if no entries were deleted, unsuccessful
            db.close();
            return false;
        }
        db.close();
        return true; //deletion was successful
    }

    //Read all values from the database and store them in an arraylist
    public ArrayList<Location> readLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Location> locArrayList = new ArrayList<>();
                if (cursor.moveToFirst()) {
            do {
                //Create a location object for each entry and add them to our arraylist
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

    //Update an entry in the database with a new address value
    public boolean updateLocation(String address, String newAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDRESSES_COL, newAddress);

        String whereClause = "address = ?";
        String[] whereArgs = {address};

        int changes = db.update(TABLE_NAME, values, whereClause, whereArgs); //update the entry with 'address' to the new address value
        if(changes == 0) { //if no entries were updated, unsuccessful
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    //Get the latitude and longitude of an address
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
                //get the first result corresponding to this address and obtain their latitude and longitude values
                int latIndex = cursor.getColumnIndex(LATITUDE_COL);
                int longIndex = cursor.getColumnIndex(LONGITUDE_COL);
                latitude = cursor.getString(latIndex);
                longitude = cursor.getString(longIndex);
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

