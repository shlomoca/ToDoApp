package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_DB_NAME = "TodosDB.db";
    private SQLiteDatabase todosDB = null;
    private SharedPreferences sp;
    private String username;
    private FloatingActionButton addTaskbtn;
    private boolean loggedOut = false;
    private ArrayList<AndroidTaskItem> androidTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        try {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            todosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

        } catch (Exception e) {
            Log.d("debug", "Error Creating Database");
        }
        addTaskbtn = findViewById(R.id.addTaskBtnID);
        addTaskbtn.setOnClickListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        // Create an ArrayList of AndroidFlavor objects
//        androidTask = new ArrayList<AndroidTaskItem>();
//        androidTask.add(new AndroidTaskItem("Android 1.6 (Donut)", "API level 6"));
//        androidTask.add(new AndroidTaskItem("Android 2.0 (Eclair)", "API level 7"));
//        androidTask.add(new AndroidTaskItem("Android 2.2 (Froyo)", "API level 8"));
//        androidTask.add(new AndroidTaskItem("Android 2.3 (GingerBread)", "API level 10"));
//        androidTask.add(new AndroidTaskItem("Android 3.0 (Honeycomb)", "API level 11"));
//        androidTask.add(new AndroidTaskItem("Android 4.0 (Ice Cream Sandwich)", "API level 15"));
//        androidTask.add(new AndroidTaskItem("Android 4.1 (Jelly Bean)", "API level 17"));
//        androidTask.add(new AndroidTaskItem("Android 4.4 (KitKat)", "API level 19"));
//        androidTask.add(new AndroidTaskItem("Android 5.0 (Lollipop)", "API level 22"));
//        androidTask.add(new AndroidTaskItem("Android 6.0 (Marshmallow)", "API level 23"));
//
//        // Create an AndroidFlavorAdapter, whose data source is a list of AndroidFlavors.
//        // The adapter knows how to create list item views for each item in the list.
//        AndroidTaskAdapter flavorAdapter = new AndroidTaskAdapter(this, androidTask);
//
//        // Get a reference to the ListView, and attach the adapter to the listView.
//        ListView listView = findViewById(R.id.ListView1ID);
//        listView.setAdapter(flavorAdapter);


    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.logoutbtn, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logoutBtnID) {
            // do something here
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    //This function logout and move to LoginActivity
    private void logout() {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        loggedOut = true;
        startActivity(loginActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mylog", "todo list onStart()");

        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        username = sp.getString("user", null);

        setTitle("Todo List (" + username + ")");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mylog", "todo list onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("mylog", "todo list onStop()");
        if (loggedOut) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user", null);
            editor.commit();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTaskBtnID:
                addTask();
                break;
        }
    }

    private void addTask() {
        Intent editorActivity = new Intent(this, EditorActivity.class);
        startActivity(editorActivity);
    }
}