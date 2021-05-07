package com.danielr_shlomoc.ex3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    private MenuItem about, exit;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Todo Login");

        dataBase = new DataBase(this);
        dataBase.createTables();

        loginBtn = findViewById(R.id.loginBtnID);
        loginBtn.setOnClickListener(this);
        userName = findViewById(R.id.userNameID);
        userPassword = findViewById(R.id.userPasswordID);
        setAcknowledgement();
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String user = sp.getString("user", null);
        if (user != null)
            login(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtnID:
                authentication();
                break;

        }
    }

    @Override
    // add 3 dots menu
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        about = menu.add("About");
        exit = menu.add("Exit");
        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog(false);
                return true;
            }
        });
        exit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog(true);
                return true;
            }
        });


        return true;
    }

    // This function creates an about dialog
    private void dialog(Boolean isExit) {
        String title, message, positive, negative;
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        if (!isExit) {
            title = "About App";
            message = "ToDoApp (com.danielr_shlomoc.ex3)\n\nBy Daniel Raz & Shlomo Carmi, 18/05/21.";
            positive = "OK";
            negative = null;
            myDialog.setIcon(R.mipmap.todo_icon_round);
        } else {
            title = "Exit App";
            message = "Do you really want to exit ToDoApp ?";
            positive = "YES";
            negative = "NO";
            myDialog.setIcon(R.drawable.ic_exit);
        }
        myDialog.setTitle(title);
        myDialog.setMessage(message);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (positive.equals("YES"))
                    finish();
            }
        });
        if (isExit) {
            myDialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        myDialog.show();
    }



    private void authentication() {

        String userName = this.userName.getText().toString();
        String userPassword = this.userPassword.getText().toString();
        boolean userExist = false;
        String[] columns = {"username", "password"};
        Cursor cr = dataBase.selectColumns(columns, "users",null);
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
                    userExist = true;
                    password = cr.getString(passwordColumn);
                    if (userPassword.equals(password))
                        login(true);
                    else
                        Toast.makeText(this, "Wrong password please try again", Toast.LENGTH_LONG).show();

                    break;
                }

            } while (cr.moveToNext());
            cr.close();
        }

        // add new user
        if (!userExist) {
//            addUser(userName, userPassword);
            dataBase.addUser(userName, userPassword);
            login(true);
        }

    }

    //This function login and move to ToDoListActivity
    private void login(boolean saveUser) {
        Intent toDoListActivity = new Intent(this, ToDoListActivity.class);
        if (saveUser) {
            String temp = this.userName.getText().toString();
            sp.edit().putString("user", temp).apply();
        }
        startActivity(toDoListActivity);
        this.finish();
    }

    private void setAcknowledgement() {
        userPassword.setImeOptions(EditorInfo.IME_ACTION_GO);
        userPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    authentication();
                    return true;
                }
                return false;
            }
        });
    }
}