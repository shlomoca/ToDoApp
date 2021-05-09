package com.danielr_shlomoc.ex3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {

    public static final String MY_DB_NAME = "TodosDB.db";
    private DataBase dataBase;
    private SharedPreferences sp;
    private String username;
    private FloatingActionButton addTaskbtn;
    private boolean loggedOut = false;
    private ArrayList<AndroidTaskItem> androidTask;
    private SearchView search;
    private ListView listView;
    private AndroidTaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        dataBase = new DataBase(this);
        addTaskbtn = findViewById(R.id.addTaskBtnID);
        search = findViewById(R.id.searchViewID);
        listView = findViewById(R.id.ListView1ID);
        search.setOnQueryTextListener(this);
        addTaskbtn.setOnClickListener(this);
        // add Item Click Listener
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);


        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        username = sp.getString("user", null);
        if (username == null)
            logout();


        setTitle("Todo List (" + username + ")");

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserTasks();
    }

    // This function display all user tasks in ListView
    private void getUserTasks() {
        // Create an ArrayList of AndroidTaskItem objects
        androidTask = dataBase.getTasks(username);

        // Create an taskAdapter, whose data source is a list of AndroidFlavors.
        // The adapter knows how to create list item views for each item in the list.
        taskAdapter = new AndroidTaskAdapter(this, androidTask);

        // Get a reference to the ListView, and attach the adapter to the listView.
        listView = findViewById(R.id.ListView1ID);
        listView.setAdapter(taskAdapter);
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
        sp.edit().remove("user").apply();
        startActivity(loginActivity);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (loggedOut)
//            sp.edit().remove("user").apply();


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
        editorActivity.putExtra("_id", -1);
        startActivity(editorActivity);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        /*This handle the search task*/
        ArrayList<AndroidTaskItem> temp = new ArrayList<>();
        for (int i = 0; i < androidTask.size(); i++) {
            AndroidTaskItem t = androidTask.get(i);
            String title = t.getTaskTitle().toLowerCase(), description = t.getDescription().toLowerCase();
            newText = newText.toLowerCase();
            if (title.contains(newText) || description.contains(newText))
                temp.add(new AndroidTaskItem(t.getId(), t.getTaskTitle(), t.getDescription(), Task.convertDateTime(t.getDate(), t.getTime())));
        }
        taskAdapter = new AndroidTaskAdapter(this, temp);
        listView.setAdapter(taskAdapter);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AndroidTaskItem androidTaskItem = androidTask.get(position);
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("_id", androidTaskItem.getId());
        startActivity(intent);
        Log.d("search", Integer.toString(androidTaskItem.getId()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /*This function handle long press on task on the task list and delete it*/
        Context context = this;
        AndroidTaskItem androidTaskItem = androidTask.get(position);
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        myDialog.setTitle("Delete task");
        myDialog.setMessage("are you sure that you want to delete this task?");
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBase.removeTask(androidTaskItem.getId());
                Log.d("search", Integer.toString(androidTaskItem.getId()));
                getUserTasks();
                Toast.makeText(context, "Todo was DELETED", Toast.LENGTH_LONG).show();
            }
        });
        myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        myDialog.show();


        return true;
    }
}