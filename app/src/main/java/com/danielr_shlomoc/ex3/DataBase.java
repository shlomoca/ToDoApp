package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBase {

    public static final String MY_DB_NAME = "TodosDB.db";
    private SQLiteDatabase todosDB = null;

    public DataBase(Context context) {

        try {
            todosDB = context.openOrCreateDatabase(MY_DB_NAME, context.MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.d("debug", "Error Creating Database");
        }
    }




}
