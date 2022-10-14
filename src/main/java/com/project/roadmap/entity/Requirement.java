package com.project.roadmap.entity;

import java.util.List;

import com.project.roadmap.entity.Constants.RequirementState;

public class Requirement {

    private String requirementTitle;
    private RequirementState requirementState;
    private List<Task> taskList;
    private int closedTaskCount;

    public Requirement(String requirementTitle, List<Task> taskList, RequirementState requirementState) {
        this.requirementTitle = requirementTitle;
        this.taskList = taskList;
        this.requirementState = requirementState;
        setClosedTaskCount();
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

    public int getClosedTaskCount() {
        return closedTaskCount;
    }

    public void setClosedTaskCount() {
        closedTaskCount = 0;
        for (Task task : taskList) {
            if (task.getTaskState() == Constants.TaskState.CLOSED)
                closedTaskCount++;
        }
    }

    public int getTaskCount() {
        return taskList.size();
    }

    public String getClosedTaskCountPercent(){
        return " ["+getClosedTaskCount()+"/"+getTaskCount()+"]  %"+ Math.round(((float)getClosedTaskCount()/getTaskCount()) *100);
    }

}
