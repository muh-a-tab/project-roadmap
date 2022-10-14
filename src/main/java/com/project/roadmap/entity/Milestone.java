package com.project.roadmap.entity;

import java.util.List;
import com.project.roadmap.entity.Constants.MilestoneState;

public class Milestone {

    private String milestoneTitle;
    private MilestoneState milestoneState;
    private List<Requirement> requirementList;

    public Milestone(String milestoneTitle, List<Requirement> requirementList, MilestoneState milestoneState) {
        this.milestoneTitle = milestoneTitle;
        this.requirementList = requirementList;
        this.milestoneState = milestoneState;
    }

    public String getMilestoneTitle() {
        return milestoneTitle;
    }

    public List<Requirement> getRequirementList() {
        return requirementList;
    }

    public MilestoneState getMilestoneState() {
        return milestoneState;
    }
}
