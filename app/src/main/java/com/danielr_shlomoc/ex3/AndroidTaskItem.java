package com.danielr_shlomoc.ex3;

public class AndroidTaskItem {
    private String taskTitle, description;
    private long dateTime;

    public AndroidTaskItem(String taskTitle, String description) {
//        this.dateTime = date;
        this.taskTitle = taskTitle;
        this.description = description;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public long getDateTime() {
        return dateTime;
    }
}
