package com.project.roadmap.entity;

import java.util.List;
import org.gitlab4j.api.Constants.IssueState;

public class FactoryEntity {

    public static Milestone getMilestoneInstance(String milestoneTitle, List<Requirement> requirementList, String milestoneState) {
        if (milestoneState.equalsIgnoreCase("closed")) {
            //Milestone altındaki Requirement'ların durumlarını kontrol ediyoruz
            for (Requirement requirement : requirementList) {
                // Requirement OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Milestone nesnesi dönüyoruz
                if (requirement.getRequirementState() != Constants.RequirementState.CLOSED)
                    return new Milestone(milestoneTitle, requirementList, Constants.MilestoneState.NOT_CLOSED_YET);
            }
            // Bütün Requirement'lar CLOSED durumundaysa
            return new Milestone(milestoneTitle, requirementList, Constants.MilestoneState.CLOSED);
        } else
            //Milestone OPENED durumdaysa
            return new Milestone(milestoneTitle, requirementList, Constants.MilestoneState.OPENED);
    }

    public static Requirement getRequirementInstance(String requirementTitle, List<Task> taskList, IssueState requirementState) {

        if (requirementState == IssueState.CLOSED) {
            //Requirement altındaki Task'ların durumlarını kontrol ediyoruz
            for (Task task : taskList) {
                // Task OPENED durumundaysa geriye NOT_CLOSED_YET durumunda Requirement nesnesi dönüyoruz
                if (task.getTaskState() == Constants.TaskState.OPENED)
                    return new Requirement(requirementTitle, taskList, Constants.RequirementState.NOT_CLOSED_YET);
            }
            // Bütün tasklar CLOSED durumundaysa
            return new Requirement(requirementTitle, taskList, Constants.RequirementState.CLOSED);
        } else
            // Requirement OPENED durumundaysa
            return new Requirement(requirementTitle, taskList, Constants.RequirementState.OPENED);

    }

    public static Task getTaskInstance(String taskTitle, IssueState taskState) {
        if (taskState == IssueState.CLOSED) {
            return new Task(taskTitle, Constants.TaskState.CLOSED);
        } else {
            return new Task(taskTitle, Constants.TaskState.OPENED);
        }
    }
}
