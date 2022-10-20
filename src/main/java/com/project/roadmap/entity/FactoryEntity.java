package com.project.roadmap.entity;

import com.project.roadmap.entity.Constants.MilestoneState;
import com.project.roadmap.entity.Constants.RequirementState;

import java.util.Date;
import java.util.List;

import org.gitlab4j.api.Constants.IssueState;

public class FactoryEntity {

    public static Milestone getMilestoneInstance(
            Long id,
            String milestoneTitle,
            List<Requirement> requirementList,
            String milestoneState,
            Date startDate, Date endDate) {

        Milestone milestone = Milestone.builder()
                .id(id)
                .milestoneTitle(milestoneTitle)
                .requirementList(requirementList)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        if (milestoneState.equalsIgnoreCase("closed")) {
            //Milestone altındaki Requirement'ların durumlarını kontrol ediyoruz
            for (Requirement requirement : requirementList) {
                // Requirement OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Milestone nesnesi dönüyoruz
                if (requirement.getRequirementState() != RequirementState.CLOSED) {
                    milestone.setMilestoneState(MilestoneState.NOT_CLOSED_YET);
                    return milestone;
                }
            }
            // Bütün Requirement'lar CLOSED durumundaysa
            milestone.setMilestoneState(MilestoneState.CLOSED);
            return milestone;
        } else {
            //Milestone OPENED durumdaysa
            milestone.setMilestoneState(MilestoneState.OPENED);
            return milestone;
        }
    }


    public static Requirement getRequirementInstance(
            Long id,
            String requirementTitle,
            List<Task> taskList,
            IssueState requirementState,
            Date startDate, Date endDate) {

        Requirement requirement = Requirement.builder()
                .id(id)
                .requirementTitle(requirementTitle)
                .taskList(taskList)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        if (requirementState == IssueState.CLOSED) {
            //Requirement altındaki Task'ların durumlarını kontrol ediyoruz
            for (Task task : taskList) {
                // Task OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Requirement nesnesi dönüyoruz
                if (task.getTaskState() != Constants.TaskState.CLOSED) {
                    requirement.setRequirementState(RequirementState.NOT_CLOSED_YET);
                    return requirement;
                }
            }
            // Bütün tasklar CLOSED durumundaysa
            requirement.setRequirementState(RequirementState.CLOSED);
            return requirement;
        } else {
            // Requirement OPENED durumundaysa
            requirement.setRequirementState(RequirementState.OPENED);
            return requirement;
        }

    }


    public static Task getTaskInstance(String taskTitle, IssueState taskState) {
        if (taskState == IssueState.CLOSED) {
            return new Task(taskTitle, Constants.TaskState.CLOSED);
        } else {
            return new Task(taskTitle, Constants.TaskState.OPENED);
        }
    }
}
