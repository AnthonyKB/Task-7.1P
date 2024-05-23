package com.example.sit305task71;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//This is the First part of the database that initialises all of the values in the database
//It is also responsible for creation, deletion, and upgrade of the database as well as storage of the data used in the application

//The Database is updated in this task to accommodate for 'latitude' and 'longtitude', which will be used for the map coordinates
public class Database extends SQLiteOpenHelper
{

    //Initialising the data in the database, including the database version/name
    // and the contents of the lost and found application (ie.name, id, location, etc)
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Adverts.db";
    public static final String TABLE_NAME = "adverts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IS_LOST = "isLost";
    public static final String COLUMN_IS_FOUND = "isFound";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";

    //Data added to the database 'latitude' and 'longitude' to be used in the map
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";


    //This private static is used to create entries in the database
    // On creation, it takes the values listed below as input in either integer or text
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," + //E.g. COLUMN_ID's input will be "INTEGER PRIMARY KEY"
                    COLUMN_IS_LOST + " INTEGER," +
                    COLUMN_IS_FOUND + " INTEGER," +
                    COLUMN_NAME + " TEXT," + //E.g. COLUMN_NAME's input will be "TEXT"
                    COLUMN_PHONE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_LOCATION + " TEXT," +

            //Adding Latitude and Longtitude
                    COLUMN_LATITUDE + " REAL," +
                    COLUMN_LONGITUDE + " REAL)";

    //This private static is used to delete entries in the database
    //It simply uses the "DROP TABLE IF EXISTS" clause which verifies the existence of the table
    private static final String DATABASE_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Database(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //This method is relevant to create the activity
    //It will use the 'DATABASE_CREATE' created above to create a new database instance
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    //This method is relevant when the database needs to be upgraded
    //It will use the 'DATABASE_DELETE' created above to wipe the database, then create a new one
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DATABASE_DELETE);
        onCreate(db);
    }
}

