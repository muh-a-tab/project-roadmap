package com.project.roadmap.service.Imp;

import com.project.roadmap.entity.Milestone;
import com.project.roadmap.entity.Requirement;
import com.project.roadmap.entity.Task;
import com.project.roadmap.service.IGitLabApiService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.IssueFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitLabApiService implements IGitLabApiService {

    final GitLabApi gitLabApi;
    private final String projectId;

    public GitLabApiService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
        //ProjectId ataması
        this.projectId = String.valueOf(39963710L);
    }

    @Override
    public List<Milestone> getMilestoneTitles() {
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
        } catch (GitLabApiException ignored) {

        }
        return Collections.emptyList();
    }


    @Override
    public List<Requirement> getRequirementIssues(String milestoneTitle) {

        List<Requirement> requirementList = new ArrayList<>();

        try {
            //Parametre olarak gelen milestone başlığına ait Requirement Issue Id'lerini bir diziye aktardık
            List<Long> requirementsId = gitLabApi.getIssuesApi().
                    getIssues(projectId, new IssueFilter().withMilestone(milestoneTitle)
                            .withLabels(List.of("requirement")))
                    .stream().map(i -> i.getIid()).collect(Collectors.toList());

            // Id'ler kullanılarak Requirement Title'ları ve Linklenmiş Issue'ları çağırarak
            // bir Requirement nesnesi oluşturuldu ve Requirement Listesine eklendi
            for (long requirement : requirementsId) {
                requirementList.add(new Requirement(gitLabApi.getIssuesApi().getIssue(projectId, requirement).getTitle(),
                        getTaskIssues(requirement)));
            }

            return requirementList;
        } catch (GitLabApiException ignored) {

        }
        return Collections.emptyList();
    }

    @Override
    public List<Task> getTaskIssues(Long requirementId) {
        List<Task> taskList = new ArrayList<>();

        try {
            //  Requirement Id ile linklenmiş task'lar bulundu ve task title'ları bir diziye ekledik
            List<String> tasks = gitLabApi.getIssuesApi().getIssueLinks(projectId, requirementId)
                    .stream().map(t -> t.getTitle()).collect(Collectors.toList());

            // Task title'larını kullanılarak nesne oluşturuldu ve listemize ekledik
            for (String task : tasks) {
                taskList.add(new Task(task));
            }

            return taskList;
        } catch (GitLabApiException ignored) {

        }

        return Collections.emptyList();
    }
}