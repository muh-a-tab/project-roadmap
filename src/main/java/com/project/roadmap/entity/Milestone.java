package com.project.roadmap.entity;

import java.util.List;

public class Milestone {

    private String milestoneTitle;
    private List<Requirement> requirementList;

    public Milestone(String milestoneTitle, List<Requirement> requirementList) {
        this.milestoneTitle = milestoneTitle;
        this.requirementList = requirementList;
    }

    public String getMilestoneTitle() {
        return milestoneTitle;
    }

    public List<Requirement> getRequirementList() {
        return requirementList;
    }

}
