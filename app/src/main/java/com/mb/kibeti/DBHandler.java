package com.mb.kibeti;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "tcdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.

    private static final String TABLE_NAME = "mpesa_records";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String AMOUNT_COL = "amount";
    // column for recipient
    private static final String RECIPIENT_COL = "recipient";

    // below variable id for our course duration column.
    private static final String TRANS_CODE_COL = "trans_code";

    // below variable for our course description column.
    private static final String DATE_COL = "date";
    private static final String TIME_COL = "time";
    private static final String TYPE_COL = "type";

    // below variable is for our course tracks column.
    private static final String CASHFLOW_COL = "cashflow";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AMOUNT_COL + " TEXT,"
                + TRANS_CODE_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + RECIPIENT_COL + " TEXT,"
                + CASHFLOW_COL + " TEXT,"
                + TIME_COL + " TEXT,"
                + TYPE_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
//    public void addNewCourse(String courseName, String courseDuration, String courseDescription, String courseTracks) {
    public void addTransactionOffline(Context context, String amount, String date, String time,String trans_code, String recipient, String type, String cashflow) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(AMOUNT_COL, amount);
        values.put(TRANS_CODE_COL, trans_code);
        values.put(DATE_COL, date);
        values.put(RECIPIENT_COL, recipient);
        values.put(TIME_COL, time);
        values.put(CASHFLOW_COL, cashflow);
        values.put(TYPE_COL, type);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
//        Toast.makeText(context, "Offline Data added successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}