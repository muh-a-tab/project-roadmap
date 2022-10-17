package com.project.roadmap.entity;

import java.util.Date;
import java.util.List;

import com.project.roadmap.entity.Constants.RequirementState;

public class Requirement implements Comparable<Requirement> {
    private final Long id;
    private final String requirementTitle;
    private final RequirementState requirementState;
    private final List<Task> taskList;
    private final Date startDate;
    private final Date endDate;

    public Requirement(Long id, String requirementTitle, List<Task> taskList, RequirementState requirementState,
                       Date startDate, Date endDate) {
        this.id = id;
        this.requirementTitle = requirementTitle;
        this.taskList = taskList;
        this.requirementState = requirementState;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }

    public String getRequirementTitle() {
        return requirementTitle;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public RequirementState getRequirementState() {
        return requirementState;
    }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() {return endDate; }

    @Override
    public int compareTo(Requirement o) {
        return getStartDate().compareTo(o.getStartDate());
    }
}
