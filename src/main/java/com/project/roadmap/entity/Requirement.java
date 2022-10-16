package com.project.roadmap.entity;

import java.util.List;

import com.project.roadmap.entity.Constants.RequirementState;

public class Requirement {

    private String requirementTitle;
    private RequirementState requirementState;
    private List<Task> taskList;

    public Requirement(String requirementTitle, List<Task> taskList, RequirementState requirementState) {
        this.requirementTitle = requirementTitle;
        this.taskList = taskList;
        this.requirementState = requirementState;
    }

    public String getRequirementTitle() {
        return requirementTitle;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public RequirementState getRequirementState() {
        return requirementState;
    }

}
