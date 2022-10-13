package com.project.roadmap.entity;

import java.util.List;
import org.gitlab4j.api.Constants.IssueState;
public class FactoryEntity {

    public static Milestone getMilestoneInstance(String milestoneTitle, List<Requirement> requirementList){
        return new Milestone(milestoneTitle,requirementList);
    }

    public static Requirement getRequirementInstance(String requirementTitle, List<Task> taskList, IssueState requirementState){

        if (requirementState == IssueState.CLOSED) {
            //Requirement altındaki Task'ların durumlarını kontrol ediyoruz
            for (Task task : taskList) {
                // Task OPENED durumundaysa geriye NOT_CLOSED_YET durumunu dönüyoruz
                if (task.getTaskState() == Constants.TaskState.OPENED)
                    return new Requirement(requirementTitle,taskList, Constants.RequirementState.NOT_CLOSED_YET);
            }
            // Bütün tasklar CLOSED durumundaysa
            return new Requirement(requirementTitle,taskList, Constants.RequirementState.CLOSED);
        } else
            // Requirement OPENED durumundaysa
        return new Requirement(requirementTitle,taskList, Constants.RequirementState.OPENED);

    }

    public static Task getTaskInstance(String taskTitle,IssueState taskState){
        if(taskState == IssueState.CLOSED){
            return new Task(taskTitle, Constants.TaskState.CLOSED);
        } else {
            return new Task(taskTitle, Constants.TaskState.OPENED);
        }
    }
}
