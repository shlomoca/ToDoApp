package com.danielr_shlomoc.ex3;

public class AndroidTaskItem {
    private String taskTitle, description,date,time;
    private int id;


    public AndroidTaskItem(int id, String taskTitle, String description, long dateTime) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.description = description;
        this.date = Task.convertDate(dateTime);
        this.time = Task.convertTime(dateTime);
    }

    public int getId() {
        return id;
    }

    public String getTaskTitle() {
        return taskTitle;
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
}
