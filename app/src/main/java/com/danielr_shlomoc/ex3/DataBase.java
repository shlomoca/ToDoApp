package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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

    public ArrayList<AndroidTaskItem> getTasks(String username) {
        ArrayList<AndroidTaskItem> tasksList = new ArrayList<AndroidTaskItem>();

        String sql = "SELECT _id, title, description, datetime FROM todos WHERE todos.username = " + "'" + username + "'";
        Cursor cr = todosDB.rawQuery(sql, null);

        int id = cr.getColumnIndex("_id");
        int title = cr.getColumnIndex("title");
        int description = cr.getColumnIndex("description");
        int dateTime = cr.getColumnIndex("datetime");

        if (cr.moveToFirst()) {
            do {
                tasksList.add(new AndroidTaskItem(cr.getInt(id), cr.getString(title), cr.getString(description), cr.getLong(dateTime)));
            }
            while (cr.moveToNext());
            cr.close();
        }
        return tasksList;
    }

    // This function create/open two tables in database users and todos.
    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
        todosDB.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS todos (_id integer primary key AUTOINCREMENT, username VARCHAR , title VARCHAR, description VARCHAR, datetime LONG);";
        todosDB.execSQL(sql);
    }

    // This function get array of preferably columns from the the given table and return a Cursor to the data.
    public Cursor selectColumns(String[] columnsArray, String table) {
        String columns = "";

        for (int i = 0; i < columnsArray.length; i++) {
            columns += i == columnsArray.length - 1 ? columnsArray[i] : columnsArray[i] + ", ";
        }

        String query = "SELECT " + columns + " FROM " + table;
        return todosDB.rawQuery(query, null);
    }

    public Cursor getTask(int id) {
        String columns[] = {"_id", "title", "description", "datetime"};

        Cursor cr = selectColumns(columns,"todos");
        if (cr.moveToFirst()) {
//            int id = cr.getColumnIndex("_id");
            int taskTitle = cr.getColumnIndex("title");
            int description = cr.getColumnIndex("description");
            int dateTime = cr.getColumnIndex("datetime");
            do {

            }
            while (cr.moveToNext());
            cr.close();
        }
        return cr;
    }

    // This function get username and his password and added the user to database
    public void addUser(String userName, String userPassword) {
        String addUser = "INSERT INTO users (username, password) VALUES ('" + userName + "', '" + userPassword + "');";
        todosDB.execSQL(addUser);
    }

    // This function get username and add his task to database
    public void addTask(String username, Task t) {
        String addTask = "INSERT INTO todos (username , title , description , datetime ) VALUES ('" + username + "', '" + t.getTitle() + "', '" + t.getDescription() + "', '" + t.convertDateTime(t.getDate(), t.getTime()) + "');";
        todosDB.execSQL(addTask);
        String[] idColumn = {"_id"};
        Cursor cr = selectColumns(idColumn, "todos");

        int id = cr.getColumnIndex("_id");

        if (cr.moveToFirst()) {
            do {
                t.setId(cr.getInt(id));
            }
            while (cr.moveToNext());
            cr.close();
        }

    }

    public void removeTask(int id) {
        String sql = "DELETE FROM todos WHERE _id = " + id;
        todosDB.execSQL(sql);
    }


}
