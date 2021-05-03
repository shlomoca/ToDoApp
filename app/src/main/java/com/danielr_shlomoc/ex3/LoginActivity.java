package com.danielr_shlomoc.ex3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_DB_NAME = "TodosDB.db";
    private SQLiteDatabase todosDB = null;
    private Button loginBtn;
    private EditText userName, userPassword;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Todo Login");
        Log.d("mylog", "onCreate()\n");


        try {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            todosDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'users' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            todosDB.execSQL(sql);
            sql = "CREATE TABLE IF NOT EXISTS todos (id integer primary key , username VARCHAR , title VARCHAR, description VARCHAR, datatime LONG);";
            todosDB.execSQL(sql);
        } catch (Exception e) {
            Log.d("debug", "Error Creating Database");
        }

        loginBtn = findViewById(R.id.loginBtnID);
        loginBtn.setOnClickListener(this);
        userName = findViewById(R.id.userNameID);
        userPassword = findViewById(R.id.userPasswordID);

        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtnID:
                authentication();
                break;

        }
    }

    private void authentication() {

        String userName = this.userName.getText().toString();
        String userPassword = this.userPassword.getText().toString();
        boolean userExist = false;

        String query = "SELECT username, password FROM users";
        Cursor cr = todosDB.rawQuery(query, null);
        String password = "";

        int usernameColumn = cr.getColumnIndex("username");
        int passwordColumn = cr.getColumnIndex("password");

        // missing one or more details.
        if (userPassword.length() == 0 || userName.length() == 0) {
            Toast.makeText(this, "One or more details missing", Toast.LENGTH_LONG).show();
            return;
        }

        if (cr.moveToFirst()) {
            do {

                String name = cr.getString(usernameColumn);

                // user exist check the password
                if (userName.equals(name)) {
                    Log.d("mylog", "user exist");
                    userExist = true;
                    password = cr.getString(passwordColumn);

                    if (userPassword.equals(password))
                        login();
                    else
                        Toast.makeText(this, "Wrong password please try again", Toast.LENGTH_LONG).show();

                    break;
                }

            } while (cr.moveToNext());
            cr.close();
        }

        // add new user
        if (!userExist) {
            addUser(userName, userPassword);
            login();
        }

    }

    //This function login and move to ToDoListActivity
    private void login() {
        Intent toDoListActivity = new Intent(this, ToDoListActivity.class);
        startActivity(toDoListActivity);
    }

    //This function add new user to database
    private void addUser(String userName, String userPassword) {
        String addUser = "INSERT INTO users (username, password) VALUES ('" + userName + "', '" + userPassword + "');";
        todosDB.execSQL(addUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mylog", "onStart()\n");// check if already user loges in
//        if (sp.getString("user", null) != null)
//            login();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("mylog", "onStart()\n");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mylog", "onPause()\n");
        String temp = this.userName.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", temp);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mylog", "onResume()\n");

    }
}