package com.project.roadmap.service;

import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;
import org.gitlab4j.api.GitLabApiException;

import java.util.List;

public interface IGitLabApiService {
    List<Milestone> getMilestoneTitles();

    List<Requirement> getRequirementIssues(String milestoneTitle);

    List<Task> getTaskIssues(Long requirementId);
}
