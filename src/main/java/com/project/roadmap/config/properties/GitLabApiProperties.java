package com.project.roadmap.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gitlab.api")
public class GitLabApiProperties {

    private String hostUrl;
    private String privateToken;
    private static Long projectId;

    public static Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        GitLabApiProperties.projectId = projectId;
    }
}
