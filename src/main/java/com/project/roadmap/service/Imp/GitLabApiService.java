package com.project.roadmap.service.Imp;

import com.project.roadmap.service.IGitLabApiService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitLabApiService implements IGitLabApiService {

    @Autowired
    GitLabApi gitLabApi;

    @Override
    public List<String> getMilestoneTitles() {
        try {
            List<String> milestoneTitles = gitLabApi.getMilestonesApi().getMilestones(39963710L)
                    .stream().map(m -> m.getTitle()).collect(Collectors.toList());
            return milestoneTitles;
        }
        catch (GitLabApiException ignored)
        {

        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getRequirementIssues(){
        return Collections.emptyList();
    }
}
