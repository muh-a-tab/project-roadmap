package com.project.roadmap.service.Imp;

import com.project.roadmap.config.properties.GitLabApiProperties;
import com.project.roadmap.entity.FactoryEntity;
import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;
import com.project.roadmap.service.GitLabApiService;

import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Issue;
import org.gitlab4j.api.models.IssueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GitLabApiServiceImp implements GitLabApiService {

    private final GitLabApi gitLabApi;
    private final String projectId = String.valueOf(GitLabApiProperties.getProjectId());
    private Logger logger = LoggerFactory.getLogger(GitLabApiService.class);
    private List<Milestone> unplacedMilestones = new ArrayList<>();
    @Override
    public List<Milestone> getProjectRoadmapStatus() {
        List<Milestone> milestoneList = new ArrayList<>();
        unplacedMilestones.clear();
        checkIssue();

        try {
            //Milestone title'larının diziye aktardık
            List<org.gitlab4j.api.models.Milestone> milestones = gitLabApi.getMilestonesApi().getMilestones(projectId);

            //Milestone title'larını ve bu Milestone'a ait Requirement Issue'larını çağırarak
            // bir Milestone nesnesi oluşturuldu. Oluşturulan bu Milestone'u Listemize ekledik
            for (org.gitlab4j.api.models.Milestone milestone : milestones) {
                Date startDate = milestone.getStartDate();
                Date endDate = milestone.getDueDate();

                // Requirement'ın başlangıç ve bitiş tarihi Milestone ile aynı çünkü issuelara start date girilemiyor.
                // O nedenle bir requirement, Milestone başlangıcı ile başlamış olur ve milestone ile sonlanır şeklinde
                // varsayımda bulunuldu ve Requirement'ın başlangıç&bitiş tarihi verildi.
                Milestone milestoneEntity = FactoryEntity.getMilestoneInstance(
                        milestone.getId(),
                        milestone.getTitle(),
                        getRequirementIssues(milestone.getTitle(), startDate, endDate),
                        milestone.getState(),
                        startDate,
                        endDate);

                if (startDate == null || endDate == null) {
                    // startDate veya endDate'i null olan milestonlerı unplacedMilestones adındaki diziye ekliyoruz
                    unplacedMilestones.add(milestoneEntity);
                } else {
                    milestoneList.add(milestoneEntity);
                }
            }
            return milestoneList.stream()
                    .sorted(Comparator.comparingLong(milestone -> milestone.getStartDate().getTime()))
                    .collect(Collectors.toList());
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getProjectRoadmapStatus() : " + e.getMessage());
        }
        return Collections.emptyList();
    }


    private List<Requirement> unplacedRequirements = new ArrayList<>();
    private List<Task> unplacedTasks = new ArrayList<>();

    public void checkIssue() {
        unplacedRequirements.clear();
        unplacedTasks.clear();
        List<Long> _placedTasks = new ArrayList<>();
        try {
            List<Issue> issues = gitLabApi.getIssuesApi().getIssues(projectId);

            for (Issue issue : issues) {

                if (issue.getLabels().contains("requirement") && issue.getMilestone() == null) {
                    unplacedRequirements.add(FactoryEntity.getRequirementInstance(issue.getId(), issue.getTitle(),
                            null, issue.getState(), issue.getCreatedAt(), issue.getClosedAt()));
                } else if (issue.getLabels().contains("requirement")) {
                    _placedTasks.addAll(gitLabApi.getIssuesApi().getIssueLinks(projectId, issue.getIid()).stream().map(i -> i.getIid()).collect(Collectors.toList()));
                }

            }
            for (Issue issue : issues) {
                if (!_placedTasks.contains(issue.getIid()) && !issue.getLabels().contains("requirement")) {
                    unplacedTasks.add(FactoryEntity.getTaskInstance(issue.getTitle(), issue.getState(), issue.getWebUrl()));
                }
            }
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> checkIssue() : " + e.getMessage());
        }

    }

    public List<Requirement> getUnplacedRequirements() {
        return unplacedRequirements;
    }
    public List<Task> getUnplacedTasks() {
        return unplacedTasks;
    }
    @Override
    public List<Milestone> getUnplacedMilestones() {
        return unplacedMilestones;
    }

    private List<Requirement> getRequirementIssues(String milestoneTitle, Date startDate, Date endDate) {
        List<Requirement> requirementList = new ArrayList<>();

        try {
            //Parametre olarak gelen milestone başlığına ait Requirement Issue Listesini tutuyoruz
            List<Issue> requirementsIssueList = gitLabApi.getIssuesApi().getIssues(
                    projectId, new IssueFilter().withMilestone(milestoneTitle).withLabels(List.of("requirement")));

            // Requirement Issue listesi ile Requirement Title'ları ve Linklenmiş Issue'ları çağırarak
            // bir Requirement nesnesi oluşturuldu ve Requirement Listesine eklendi
            for (Issue requirement : requirementsIssueList) {
                Requirement requirementEntity = FactoryEntity.getRequirementInstance(
                        requirement.getId(),
                        requirement.getTitle(),
                        getTaskIssues(requirement.getIid()), requirement.getState(), startDate, endDate);
                requirementList.add(requirementEntity);
            }
            return requirementList.stream()
                    .sorted(Comparator.comparingLong(requirement -> requirement.getStartDate().getTime()))
                    .collect(Collectors.toList());
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getRequirementIssues() : " + e.getMessage());
        }
        return Collections.emptyList();
    }


    private List<Task> getTaskIssues(Long requirementId) {
        List<Task> taskList = new ArrayList<>();

        try {
            //  Requirement Id ile linklenmiş task'lar bulundu ve diziye ekledik
            List<Issue> tasks = gitLabApi.getIssuesApi().getIssueLinks(projectId, requirementId);

            // Task dizisi kullanılarak Task nesneleri oluşturuldu ve listemize ekledik
            for (Issue task : tasks) {
                // Task'ın milestone'u kontrol ediliyor
                taskList.add(FactoryEntity.getTaskInstance(task.getTitle(), task.getState(), task.getWebUrl()));
            }
            return taskList;
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getTaskIssues() : " + e.getMessage());
        }

        return Collections.emptyList();
    }
}