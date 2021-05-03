package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBase {

    public static final String MY_DB_NAME = "TodosDB.db";
    private SQLiteDatabase todosDB = null;
    private String username;

    public DataBase(Context context, String username) {
        this.username = username;

        try {
            todosDB = context.openOrCreateDatabase(MY_DB_NAME, context.MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.d("debug", "Error Creating Database");
        }
    }

//    public ArrayList<AndroidTaskItem> getTasks(String username){
//
//    }

    public void addTask() {

    }


}
