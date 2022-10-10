package com.project.roadmap.service;

import org.gitlab4j.api.GitLabApiException;

import java.util.List;

public interface IGitLabApiService {

    public List<String> getMilestoneTitles();

    public List<String> getRequirementIssues ();

}
