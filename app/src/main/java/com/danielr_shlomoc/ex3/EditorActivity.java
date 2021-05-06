package com.danielr_shlomoc.ex3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button addBtn, dateBtn, timeBtn;
    private EditText timeEdt, dateEdt, descEdt, titleEdt;
    private TextView title;
    private SharedPreferences sp;
    private int dateLen, timeLen, taskID;
    private String username;
    private DataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Todo Editor");
        dateLen = 10;
        timeLen = 5;
        connectWidgets();
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        username = sp.getString("user", null);
        taskID = getIntent().getIntExtra("_id",-1);
        if (taskID > -1) {
            Task task = new Task(taskID);
            Log.d("id",Integer.toString(this.taskID));
//            loadTask(task);
        }
        dataBase = new DataBase(this);


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        SharedPreferences.Editor editor = sp.edit();
        editor.remove("taskID");
        editor.apply();

        SharedPreferences.Editor pauseEditor = getSharedPreferences("saved_editor", Context.MODE_PRIVATE).edit();
        pauseEditor.clear();
        pauseEditor.remove("saved_editor");
        pauseEditor.remove("taskID");
        pauseEditor.remove("date");
        pauseEditor.remove("time");
        pauseEditor.remove("title");
        pauseEditor.remove("description");
        pauseEditor.remove("pageTitle");
        pauseEditor.remove("addButton");
        pauseEditor.apply();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScreen();

    }

    private void saveScreen() {
        //puts all of the screen in the shared preferences

        SharedPreferences preferences = getSharedPreferences("saved_editor", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();
        String date = dateEdt.getText().toString();
        String time = timeEdt.getText().toString();
        String titleEdit = titleEdt.getText().toString();
        String description = descEdt.getText().toString();
        String pageTitle = title.getText().toString();
        String addButton = addBtn.getText().toString();
        e.putBoolean("saved_editor", true);
        e.putInt("taskID", taskID);
        e.putString("date", date);
        e.putString("time", time);
        e.putString("title", titleEdit);
        e.putString("description", description);
        e.putString("pageTitle", pageTitle);
        e.putString("addButton", addButton);
        e.apply();
    }

    private void loadScreen() {
        //loads all screen elements to their place the shared preferences

        SharedPreferences preferences = getSharedPreferences("saved_editor", Context.MODE_PRIVATE);
//        SharedPreferences.Editor e = preferences.edit();
//        if (false) {
        if (preferences.getBoolean("saved_editor", false)) {
            String date = preferences.getString("date", "");
            String time = preferences.getString("time", "");
            String titleEdit = preferences.getString("title", "");
            String description = preferences.getString("description", "");
            String pageTitle = preferences.getString("pageTitle", "");
            String addButton = preferences.getString("addButton", "");
            taskID = preferences.getInt("taskID", -1);
            title.setText(pageTitle);
            addBtn.setText(addButton);
            timeEdt.setText(time);
            dateEdt.setText(date);
            titleEdt.setText(titleEdit);
            descEdt.setText(description);
        }
    }

    private void connectWidgets() {
        //connects all widgets and sets them listeners
        addBtn = findViewById(R.id.add_task_btn);
        dateBtn = findViewById(R.id.pick_date_btn);
        timeBtn = findViewById(R.id.pick_time_btn);
        timeEdt = findViewById(R.id.time_input_edt);
        dateEdt = findViewById(R.id.date_input_edt);
        descEdt = findViewById(R.id.description_input_edt);
        titleEdt = findViewById(R.id.title_input_edt);
        title = findViewById(R.id.task_title_txt);

        addBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);

        timeEdt.addTextChangedListener(createTextWatcher(timeLen));
        dateEdt.addTextChangedListener(createTextWatcher(dateLen));
    }

    private Task createTask() {
        String date = dateEdt.getText().toString();
        String time = timeEdt.getText().toString();
        String title = titleEdt.getText().toString();
        String description = descEdt.getText().toString();
        return new Task(title, description, date, time, taskID);
    }

    private void loadTask(Task t) {
        //load an existing task to the screen

        Cursor cr = dataBase.getTask(this.taskID);

        if (cr.moveToFirst()) {
            int id = cr.getColumnIndex("_id");
            int taskTitle = cr.getColumnIndex("title");
            int description = cr.getColumnIndex("description");
            int dateTime = cr.getColumnIndex("datetime");
            do {
                titleEdt.setText(cr.getString(taskTitle));
                descEdt.setText(cr.getString(description));
                dateEdt.setText(Task.convertDate(cr.getLong(dateTime)));
                timeEdt.setText(Task.convertTime(cr.getLong(dateTime)));
            }
            while (cr.moveToNext());
            cr.close();
        }

//        titleEdt.setText(t.getTitle());
//        descEdt.setText(t.getDescription());
//        dateEdt.setText(t.getDate());
//        timeEdt.setText(t.getTime());
        String s = String.format("%s%d)", getString(R.string.update_Todo), taskID);
        title.setText(s);
        addBtn.setText(R.string.update);
    }

    private TextWatcher createTextWatcher(int letters) {
        //creates a textWatcher that hides the keyboard when Editable gets to letters
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == letters)
                    HideKeyboardFormUser();
            }
        };
    }

    private void HideKeyboardFormUser() {
        View view = getCurrentFocus();
        InputMethodManager hideKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null)
            hideKeyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void saveTask(Task t) {
        Log.i("tester", t.toString());
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_task_btn:
                try {
                    Task t = createTask();
                    dataBase.addTask(this.username,t);
                    saveTask(t);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.pick_date_btn:
                com.danielr_shlomoc.ex3.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.danielr_shlomoc.ex3.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                break;
            case R.id.pick_time_btn:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                break;

        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeEdt.setText("" + hourOfDay + ":" + minute);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar mCalender = Calendar.getInstance();
        mCalender.set(Calendar.YEAR, year);
        mCalender.set(Calendar.MONTH, month);
        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date calenderDate = mCalender.getTime();
        dateEdt.setText(dateFormat.format(calenderDate));
    }

}