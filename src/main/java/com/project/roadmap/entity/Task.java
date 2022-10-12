package com.project.roadmap.entity;

import com.project.roadmap.Constants.TaskState;

public class Task {

    private final String taskTitle;

    private TaskState taskState;

    public Task(String taskTitle, TaskState taskState) {
        this.taskTitle = taskTitle;
        this.taskState = taskState;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public TaskState getTaskState() {
        return taskState;
    }
}
