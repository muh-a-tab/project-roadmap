package com.project.roadmap.service.Imp;

import com.project.roadmap.config.GitLabApiConfiguration;
import com.project.roadmap.config.properties.GitLabApiProperties;
import com.project.roadmap.entity.FactoryEntity;
import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;
import com.project.roadmap.service.IGitLabApiService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Issue;
import org.gitlab4j.api.models.IssueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitLabApiService implements IGitLabApiService {

    private Logger logger = LoggerFactory.getLogger(GitLabApiService.class);
    final GitLabApi gitLabApi;

    //ProjectId ataması
    private String projectId;

    public GitLabApiService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
        this.projectId = String.valueOf(GitLabApiProperties.getProjectId());
    }

    @Override
    public List<Milestone> getProjectRoadmapStatus() {
        List<Milestone> milestoneList = new ArrayList<>();
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
                Milestone milestoneEntity = FactoryEntity.getMilestoneInstance(milestone.getId(), milestone.getTitle(),
                        getRequirementIssues(milestone.getTitle(), startDate, endDate), milestone.getState(),
                        startDate, endDate);
                milestoneList.add(milestoneEntity);
            }
            Collections.sort(milestoneList);
            return milestoneList;
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getProjectRoadmapStatus() : " + e.getMessage());
        }
        return Collections.emptyList();
    }

    List<Requirement> unplacedRequirement = new ArrayList<>();
    List<Task> unplacedTask = new ArrayList<>();

    public void checkIssue() {
        List<Requirement> _unplacedRequirement = new ArrayList<>();
        List<Task> _unplacedTask = new ArrayList<>();
        List<Long> _placedTask = new ArrayList<>();
        try {
            List<Issue> issues = gitLabApi.getIssuesApi().getIssues(projectId);

            for (Issue issue : issues) {

                if (issue.getLabels().contains("requirement") && issue.getMilestone() == null) {
                    _unplacedRequirement.add(FactoryEntity.getRequirementInstance(issue.getId(), issue.getTitle(),
                            null, issue.getState(), issue.getCreatedAt(), issue.getClosedAt()));
                    } else if (issue.getLabels().contains("requirement")) {
                    _placedTask.addAll(gitLabApi.getIssuesApi().getIssueLinks(projectId, issue.getIid()).stream().map(i -> i.getIid()).collect(Collectors.toList()));
                }

            }
            for (Issue issue : issues) {
                if (!_placedTask.contains(issue.getIid()) && !issue.getLabels().contains("requirement")) {
                    _unplacedTask.add(FactoryEntity.getTaskInstance(issue.getTitle(), issue.getState()));
                }
            }


            unplacedRequirement = _unplacedRequirement;
            unplacedTask = _unplacedTask;

        } catch (GitLabApiException e) {

        }

    }

    @Override
    public List<Requirement> getUnplacedRequirement() {
        return unplacedRequirement;
    }

    @Override
    public List<Task> getUnplacedTask() {
        return unplacedTask;
    }

    private List<Requirement> getRequirementIssues(String milestoneTitle, Date startDate, Date endDate) {

        List<Requirement> requirementList = new ArrayList<>();

        try {
            //Parametre olarak gelen milestone başlığına ait Requirement Issue Listesini tutuyoruz
            List<Issue> requirementsIssueList = gitLabApi.getIssuesApi().
                    getIssues(projectId, new IssueFilter().withMilestone(milestoneTitle)
                            .withLabels(List.of("requirement")));

            // Requirement Issue listesi ile Requirement Title'ları ve Linklenmiş Issue'ları çağırarak
            // bir Requirement nesnesi oluşturuldu ve Requirement Listesine eklendi
            for (Issue requirement : requirementsIssueList) {
                Requirement requirementEntity = FactoryEntity.getRequirementInstance(requirement.getId(), requirement.getTitle(),
                        getTaskIssues(requirement.getIid()), requirement.getState(), startDate, endDate);
                requirementList.add(requirementEntity);
            }
            Collections.sort(requirementList);
            return requirementList;
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
                taskList.add(FactoryEntity.getTaskInstance(task.getTitle(), task.getState()));
            }

            return taskList;
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getTaskIssues() : " + e.getMessage());
        }

        return Collections.emptyList();
    }
}