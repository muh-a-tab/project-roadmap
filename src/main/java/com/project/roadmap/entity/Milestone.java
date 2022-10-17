package com.project.roadmap.entity;

import java.util.Date;
import java.util.List;
import com.project.roadmap.entity.Constants.MilestoneState;

public class Milestone implements Comparable<Milestone> {
    private final Long id;
    private final String milestoneTitle;
    private final MilestoneState milestoneState;
    private final List<Requirement> requirementList;
    private final Date startDate;
    private final Date endDate;

    public Milestone(Long id, String milestoneTitle, List<Requirement> requirementList, MilestoneState milestoneState,
                     Date startDate, Date endDate) {
        this.id = id;
        this.milestoneTitle = milestoneTitle;
        this.requirementList = requirementList;
        this.milestoneState = milestoneState;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }

    public String getMilestoneTitle() {
        return milestoneTitle;
    }

    public List<Requirement> getRequirementList() {
        return requirementList;
    }

    public MilestoneState getMilestoneState() {
        return milestoneState;
    }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() {return endDate; }

    @Override
    public int compareTo(Milestone o) {
        return getStartDate().compareTo(o.getStartDate());
    }
}
