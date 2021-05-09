package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

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
    public Cursor selectColumns(String[] columnsArray, String table,String condition) {
        String columns = "";

        for (int i = 0; i < columnsArray.length; i++) {
            columns += i == columnsArray.length - 1 ? columnsArray[i] : columnsArray[i] + ", ";
        }

        String query = "SELECT " + columns + " FROM " + table;
        if(condition != null)
            query += " WHERE "+condition;
        return todosDB.rawQuery(query, null);
    }

    public Cursor getTaskCursor(int id) {
        String columns[] = {"_id", "title", "description", "datetime"};

        Cursor cr = selectColumns(columns, "todos","_id = "+"'"+id+"';");

        return cr;
    }

    Task getTask(int id){
        Cursor cr = this.getTaskCursor(id);
        Task task = null;
        if (cr.moveToFirst()) {

            int taskTitleIndex = cr.getColumnIndex("title");
            int descriptionIndex = cr.getColumnIndex("description");
            int dateTimeIndex = cr.getColumnIndex("datetime");
            do {
                long dateTime = cr.getLong(dateTimeIndex);

                task = new Task(cr.getString(taskTitleIndex),cr.getString(descriptionIndex),Task.convertDate(dateTime),Task.convertTime(dateTime),id);

            }
            while (cr.moveToNext());
            cr.close();
        }
        return task;

    }

    // This function get username and his password and added the user to database
    public void addUser(String userName, String userPassword) {
        String addUser = "INSERT INTO users (username, password) VALUES ('" + userName + "', '" + userPassword + "');";
        todosDB.execSQL(addUser);
    }

    // This function get username and add his task to database
    public void addTask(String username, Task t) {
        String addTask = "INSERT INTO todos (username , title , description , datetime ) VALUES ('" + username + "', '" + t.getTitle() + "', '" + t.getDescription() + "', '" + t.getDateTime() + "');";
        todosDB.execSQL(addTask);
        String[] idColumn = {"_id"};
        Cursor cr = selectColumns(idColumn, "todos",null);

        int id = cr.getColumnIndex("_id");

        if (cr.moveToFirst()) {
            do {
                t.setId(cr.getInt(id));
            }
            while (cr.moveToNext());
            cr.close();
        }

    }

    public void updateTask(int id, Task t) {
        try {
            String query = "UPDATE todos SET title = " + "'" + t.getTitle() + "'" + ", description = " + "'" + t.getDescription() + "'" + ", datetime = " + "'" + t.getDateTime() + "'" + " WHERE _id = " + id + ";";
            todosDB.execSQL(query);
        } catch (Exception ignored) {

        }
    }

    public void removeTask(int id) {
        try {
            String sql = "DELETE FROM todos WHERE _id = " + id;
            todosDB.execSQL(sql);
        } catch (Exception e) {

        }
    }


}
