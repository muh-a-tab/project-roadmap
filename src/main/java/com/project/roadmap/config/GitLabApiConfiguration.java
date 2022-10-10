package com.project.roadmap.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.project.roadmap.config.properties.GitLabApiProperties;

@Configuration
public class GitLabApiConfiguration {

    @Bean
    public GitLabApi gitLabApi(GitLabApiProperties gitLabApiProperties) {

        return new GitLab4JApiBuilder(gitLabApiProperties.getHostUrl()).build(gitLabApiProperties.getPrivateToken());
    }

    private class GitLab4JApiBuilder {

        private String hostUrl;

        private GitLab4JApiBuilder(String hostUrl) {
            this.hostUrl = hostUrl;
        }

        private GitLabApi build(String privateToken) {
            return new GitLabApi(hostUrl, privateToken);
        }
    }
}
