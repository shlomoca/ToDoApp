package com.danielr_shlomoc.ex3;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Task {
    final private static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH), DATE_F= new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) , TIME_F = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private int id;
    private String title, description, date, time;


    public Task(String title, String description, String date, String time, int id) throws IllegalArgumentException {
//        FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
//        DATE_F = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//        TIME_F = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        validateInput(title, description, date, time);
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.id = id;

    }
    public Task(int id) throws IllegalArgumentException {
//        FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
//        DATE_F = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//        TIME_F = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        loadTaskFromDB(id);
//        this.id = id;
    }

    private void loadTaskFromDB(int id) {
//        this.title = title;
//        this.description = description;
//        this.date = date;
//        this.time = time;

    }
    private void saveTaskInDB() {
    //        this.title = title;
    //        this.description = description;
    //        this.date = date;
    //        this.time = time;
        }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "task " + title + " in date " +
                date +" "+
                time  + " description: " + description;
    }

    /*checks that the title and description contain at least one character
     and test the date/time to see if it is valid*/
    private void validateInput(String title, String description, String date, String time) {
        if (title.length() < 1)
            throw new IllegalArgumentException("Please enter title");
        if (description.length() < 1)
            throw new IllegalArgumentException("Please enter description");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            timeF.parse(time);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal time. time format is hh:mm");
        }
        try {
            DATE_F.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal date. date format is dd/mm/yyyy");
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        return TIME_F.format(date);
    }

    public static String convertDate(long time) {
        Date date = new Date(time);
        Log.d("date",DATE_F.format(date));
        return DATE_F.format(date);
    }

    public static long convertDateTime(String date, String time) {
        //convert date and time to long
        try {
            Date d = FORMATTER.parse(date + " " + time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getDateTime() {
        return convertDateTime(date,time);
    }
}
