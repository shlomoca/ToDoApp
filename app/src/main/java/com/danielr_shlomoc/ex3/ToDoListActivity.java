package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_DB_NAME = "TodosDB.db";
    private SQLiteDatabase todosDB = null;
    private SharedPreferences sp;
    private String username;
    private FloatingActionButton addTaskbtn;
    private boolean loggedOut = false;

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