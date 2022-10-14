package com.project.roadmap.entity;

public interface Constants {

    enum TaskState {
        OPENED, CLOSED
    }

    enum RequirementState {
        OPENED, CLOSED, NOT_CLOSED_YET
    }

    enum MilestoneState {
        OPENED, CLOSED, NOT_CLOSED_YET
    }

}
