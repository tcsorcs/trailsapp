package com.tcsorcs.trailsapp.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.widget.Toast;

import com.tcsorcs.trailsapp.helpers.Location;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLDataException;

/**
 * Created by Innovation on 3/3/2015.
 */
public class TrailAppDbHelper extends SQLiteOpenHelper {
    // IF you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_LOCATION = "/data/data/com.tcsorcs.trailsapp/databases/";

    public static final String DATABASE_NAME = "TrailApp.db";

    private final Context myContext;

    private SQLiteDatabase myDataBase;

    public TrailAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    // Create an empty database on the system and rewrites with pre-loaded database
    public void createDataBase() throws IOException {


        boolean databaseExists=false;
        try{
             databaseExists = checkDataBase();

        }catch (SQLDataException e){
            databaseExists = false;
            e.printStackTrace();
        }catch (SQLiteCantOpenDatabaseException e){
            //TODO does this throw only when database doesn't exist? we don't want to overwrite database if it can't open a connection for other reasons? research neeeded
            //was throwing this exception when trying to open database that didn't exist, setting boolean value to false to signal copy db
            databaseExists = false;
            e.printStackTrace();
        }

        if(databaseExists){
            //do nothing
        }else{
            // create empty database to be overwritten
            this.getReadableDatabase();

            try {
                //Toast.makeText(DisplayManager.getInstance().main_activity, "Creating DB", Toast.LENGTH_LONG).show();

                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }

    }

    // check for database to already exist returns true if it exists false if it doesn't
    private boolean checkDataBase() throws SQLDataException{
        SQLiteDatabase checkDB = null;

        String myPath = DATABASE_LOCATION + DATABASE_NAME;
        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    // Copies data from assets folder to the empty database
    private void copyDataBase() throws IOException{

        //Open local database as input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        //path to empty database
        String outFileName = DATABASE_LOCATION + DATABASE_NAME;

        // OPen empty database as output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfers bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    //open the database
    public void openDataBase() throws SQLDataException{
        String myPath = DATABASE_LOCATION + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized void close(){
        if(myDataBase != null)
            myDataBase.close();

        super.close();
    }

    // CRUD (Create, Read, Update, Delete)
    public Location getLocation(String currentScan){

        //TODO query database for location x, y based on location name: currentScan
        SQLiteDatabase db = this.getReadableDatabase();


        //FOR TESTING WHAT TABLES ARE IN DB
        //DISPLAYS TABLE NAMES TO TOAST MESSAGE ON DEVICE
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        if (c.moveToFirst()) {
//            while ( !c.isAfterLast() ) {
//                Toast.makeText(DisplayManager.getInstance().main_activity, "Table Name=> " + c.getString(0), Toast.LENGTH_LONG).show();
//                c.moveToNext();
//            }
//        }
//        Toast.makeText(DisplayManager.getInstance().main_activity, "Table Names END", Toast.LENGTH_LONG).show();
        //END ---DISPLAYS TABLE NAMES TO TOAST MESSAGE ON DEVICE


        Cursor cursor = db.query(TrailAppDbContract.Locations.TABLE_NAME,
                new String[]{TrailAppDbContract.Locations.COLUMN_NAME_LOCATION_ID,
                        TrailAppDbContract.Locations.COLUMN_NAME_LOCATION_X,
                        TrailAppDbContract.Locations.COLUMN_NAME_LOCATION_Y},
                TrailAppDbContract.Locations.COLUMN_NAME_LOCATION_ID + "=?",
                new String[]{ String.valueOf(currentScan)}, null , null, null);
        Location location=null;
        if(cursor != null)
            if( cursor.moveToFirst()){
                 location = new Location(cursor.getString(0), Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)));

            }
        return location;
    }
}
