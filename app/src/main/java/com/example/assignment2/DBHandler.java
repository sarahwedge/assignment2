package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.

    }

    public void addNewLocation(String address, String latitude, String longitude) {

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
        // content values to our table.
        db.insert(TABLE_NAME, null, values);


        // at last we are closing our
        // database after adding database.
        db.close();
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // this method is called to check if the table exists already.
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
//        sqLiteDatabase.execSQL("ALTER TABLE notes ADD COLUMN colour TEXT");
//        sqLiteDatabase.execSQL("ALTER TABLE notes ADD COLUMN image BLOB");
    }

    public void removeLocation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM " + TABLE_NAME + " WHERE id='%d'",id));

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
        cursor.close();

        return locArrayList;
    }



    public boolean idExists(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + IDS_COL + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }

    public void updateLocation(int id, String address, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDRESSES_COL, address);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };

        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }
}

