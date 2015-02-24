package com.thwrayman.matchar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmet on 01/12/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "userInformation";

    // users table name
    private static final String TABLE_USERS = "users";

    // users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_AGE = "age";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_JOB = "job";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT,"
                + KEY_LNAME + " TEXT" + KEY_AGE + "TEXT" + KEY_EMAIL + "TEXT"
                + KEY_PASSWORD + "TEXT" + KEY_JOB + "TEXT"+ ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new user
    public void addUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, user.getFname()); // user first name
        values.put(KEY_LNAME, user.getLname()); // user last name
        values.put(KEY_AGE,user.getAge());//age
        values.put(KEY_EMAIL, user.get_email()); // user email
        values.put(KEY_PASSWORD, user.get_password()); // user password
        values.put(KEY_JOB, user.getJob()); // user job

        // Inserting Row

        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection

    }

    // Getting single user
    public UserDetails getUser(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_FNAME, KEY_LNAME,KEY_AGE,KEY_EMAIL,KEY_PASSWORD,KEY_JOB }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserDetails user = new UserDetails(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),
                cursor.getString(5));

        // return user
        return user;
    }

    // Getting All users
    public List<UserDetails> getAllUsers() {
        List<UserDetails> UserList = new ArrayList<UserDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserDetails user = new UserDetails();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setAge(cursor.getString(1));
                user.setFname(cursor.getString(2));
                user.setLname(cursor.getString(3));
                user.set_email(cursor.getString(4));
                user.setJob(cursor.getString(5));
                user.set_password(cursor.getString(6));

                // Adding user to list
                UserList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return UserList;

    }

    // Getting users Count
    public int getUsersCount() {

        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // Updating single user
    public int updateUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, user.getFname());
        values.put(KEY_LNAME, user.getLname());
        values.put(KEY_EMAIL, user.get_email());
        values.put(KEY_PASSWORD, user.get_password());
        values.put(KEY_JOB, user.getJob());
        values.put(KEY_AGE, user.getAge());


        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });

    }

    // Deleting single user
    public void deleteUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getID()) });
        db.close();

    }

    public void getLastRecord(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + "sqlite_sequence";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();

    }


}
