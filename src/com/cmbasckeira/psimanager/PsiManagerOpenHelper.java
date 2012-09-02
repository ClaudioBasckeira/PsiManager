package com.cmbasckeira.psimanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PsiManagerOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    
    private static final String DATABASE_NAME = "PsiManager";
    
    private static final String POWERS_TABLE_NAME = "powers";
    private static final String LISTS_TABLE_NAME = "lists";
    private static final String LISTS_REL_POWERS_TABLE_NAME = "lists_rel_powers";
    
    private static final String POWERS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + POWERS_TABLE_NAME + " (" +
            		"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            		"name TEXT," +
            		"description TEXT," +
            		"duration INTEGER," +
            		"cost INTEGER);";
    
    private static final String LISTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + POWERS_TABLE_NAME + " (" +
            		"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            		"name TEXT," +
            		"description TEXT," +
            		"points INTEGER);";
    
    private static final String LISTS_REL_POWERS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + POWERS_TABLE_NAME + " (" +
            		"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            		"ID_LIST INTEGER," +
            		"ID_POWER INTEGER);";

    PsiManagerOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POWERS_TABLE_CREATE);
        db.execSQL(LISTS_TABLE_CREATE);
        db.execSQL(LISTS_REL_POWERS_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	db.execSQL("DROP TABLE IF EXISTS " + POWERS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + LISTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + LISTS_REL_POWERS_TABLE_NAME);
		onCreate(db);
    }
}
