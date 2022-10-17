package com.project.roadmap.entity;

import java.util.Date;
import java.util.List;
import org.gitlab4j.api.Constants.IssueState;

public class FactoryEntity {

    public static Milestone getMilestoneInstance(Long id, String milestoneTitle, List<Requirement> requirementList, String milestoneState,
                                                 Date startDate, Date endDate) {
        if (milestoneState.equalsIgnoreCase("closed")) {
            //Milestone altındaki Requirement'ların durumlarını kontrol ediyoruz
            for (Requirement requirement : requirementList) {
                // Requirement OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Milestone nesnesi dönüyoruz
                if (requirement.getRequirementState() != Constants.RequirementState.CLOSED)
                    return new Milestone(id, milestoneTitle, requirementList, Constants.MilestoneState.NOT_CLOSED_YET, startDate, endDate);
            }
            // Bütün Requirement'lar CLOSED durumundaysa
            return new Milestone(id, milestoneTitle, requirementList, Constants.MilestoneState.CLOSED, startDate, endDate);
        } else
            //Milestone OPENED durumdaysa
            return new Milestone(id, milestoneTitle, requirementList, Constants.MilestoneState.OPENED, startDate, endDate);
    }

    public static Requirement getRequirementInstance(Long id, String requirementTitle, List<Task> taskList, IssueState requirementState,
                                                     Date startDate, Date endDate) {
        if (requirementState == IssueState.CLOSED) {
            //Requirement altındaki Task'ların durumlarını kontrol ediyoruz
            for (Task task : taskList) {
                // Task OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Requirement nesnesi dönüyoruz
                if (task.getTaskState() != Constants.TaskState.CLOSED)
                    return new Requirement(id, requirementTitle, taskList, Constants.RequirementState.NOT_CLOSED_YET, startDate, endDate);
            }
            // Bütün tasklar CLOSED durumundaysa
            return new Requirement(id, requirementTitle, taskList, Constants.RequirementState.CLOSED, startDate, endDate);
        } else
            // Requirement OPENED durumundaysa
            return new Requirement(id, requirementTitle, taskList, Constants.RequirementState.OPENED, startDate, endDate);
    }

    public static Task getTaskInstance(String taskTitle, IssueState taskState) {
        if (taskState == IssueState.CLOSED) {
            return new Task(taskTitle, Constants.TaskState.CLOSED);
        } else {
            return new Task(taskTitle, Constants.TaskState.OPENED);
        }
    }
}
