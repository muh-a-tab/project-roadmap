package com.project.roadmap.service;

import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;

import java.util.List;

public interface GitLabApiService {

    List<Milestone> getProjectRoadmapStatus();

    List<Requirement> getUnplacedRequirements();

    List<Task> getUnplacedTasks();

    List<Milestone> getUnplacedMilestones();

}
