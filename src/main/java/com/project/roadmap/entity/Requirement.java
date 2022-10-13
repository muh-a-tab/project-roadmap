package com.project.roadmap.entity;

import java.util.List;

public class Requirement {

    private String requirementTitle;
    private List<Task> taskList;

    public Requirement(String requirementTitle, List<Task> taskList) {
        this.requirementTitle = requirementTitle;
        this.taskList = taskList;
    }

    public String getRequirementTitle() {
        return requirementTitle;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
    
}
