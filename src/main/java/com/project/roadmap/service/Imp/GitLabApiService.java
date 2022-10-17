package com.project.roadmap.service.Imp;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GitLabApiService implements IGitLabApiService {

    private Logger logger = LoggerFactory.getLogger(GitLabApiService.class);
    final GitLabApi gitLabApi;
    private final String projectId;

    public GitLabApiService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
        //ProjectId ataması
        this.projectId = String.valueOf(1L);
    }

    @Override
    public List<Milestone> getProjectRoadmapStatus() {
        List<Milestone> milestoneList = new ArrayList<>();

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
                taskList.add(FactoryEntity.getTaskInstance(task.getTitle(), task.getState()));
            }

            return taskList;
        } catch (GitLabApiException e) {
            logger.warn("GitLabApiService.java -> getTaskIssues() : " + e.getMessage());
        }

        return Collections.emptyList();
    }
}