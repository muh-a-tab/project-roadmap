package com.project.roadmap.service.Imp;

import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;
import com.project.roadmap.service.IGitLabApiService;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Issue;
import org.gitlab4j.api.models.IssueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.project.roadmap.entity.Constants.TaskState;
import com.project.roadmap.entity.Constants.RequirementState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitLabApiService implements IGitLabApiService {

    private Logger logger = LoggerFactory.getLogger(GitLabApiService.class);
    final GitLabApi gitLabApi;
    private final String projectId;

    public GitLabApiService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
        //ProjectId ataması
        this.projectId = String.valueOf(34L);
    }

    @Override
    public List<Milestone> getProjectRoadmapStatus() {
        List<Milestone> milestoneList = new ArrayList<>();

        try {
            //Milestone title'larının diziye aktardık
            List<String> milestones = gitLabApi.getMilestonesApi().getMilestones(projectId)
                    .stream().map(m -> m.getTitle()).collect(Collectors.toList());

            //Milestone title'larını ve bu Milestone'a ait Requirement Issue'larını çağırarak
            // bir Milestone nesnesi oluşturuldu. Oluşturulan bu Milestone'u Listemize ekledik
            for (String milestone : milestones) {
                milestoneList.add(new Milestone(milestone, getRequirementIssues(milestone)));
            }

            return milestoneList;
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getProjectRoadmapStatus() : " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private List<Requirement> getRequirementIssues(String milestoneTitle) {

        List<Requirement> requirementList = new ArrayList<>();

        try {
            //Parametre olarak gelen milestone başlığına ait Requirement Issue Listesini tutuyoruz
            List<Issue> requirementsIssueList = gitLabApi.getIssuesApi().
                    getIssues(projectId, new IssueFilter().withMilestone(milestoneTitle)
                            .withLabels(List.of("requirement")));

            // Requirement Issue listesi ile Requirement Title'ları ve Linklenmiş Issue'ları çağırarak
            // bir Requirement nesnesi oluşturuldu ve Requirement Listesine eklendi
            for (Issue requirement : requirementsIssueList) {
                requirementList.add(new Requirement(requirement.getTitle(),
                        getTaskIssues(requirement.getIid()), requirementStateCheck(requirement)));
            }

            return requirementList;
        } catch (GitLabApiException ignored) {

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
                taskList.add(new Task(task.getTitle(), task.getState() == Constants.IssueState.CLOSED ? TaskState.CLOSED : TaskState.OPENED));
            }

            return taskList;
        } catch (GitLabApiException ignored) {

        }

        return Collections.emptyList();
    }

    private RequirementState requirementStateCheck(Issue requirement) {

        // Requirement CLOSED durumundaysa
        if (requirement.getState() == Constants.IssueState.CLOSED) {

            //Requirement altındaki Task'ların durumlarını kontrol ediyoruz
            for (Task task : getTaskIssues(requirement.getIid())) {
                // Task OPENED durumundaysa geriye NOT_CLOSED_YET durumunu dönüyoruz
                if (task.getTaskState() == TaskState.OPENED)
                    return RequirementState.NOT_CLOSED_YET;
            }
            // Bütün tasklar CLOSED durumundaysa
            return RequirementState.CLOSED;
        } else
            // Requirement OPENED durumundaysa
            return RequirementState.OPENED;
    }

}
