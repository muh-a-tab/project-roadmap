package com.project.roadmap.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gitlab.api")
public class GitLabApiProperties {

	private String hostUrl;

	private String privateToken;

	private static Long projectId;

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getPrivateToken() {
		return privateToken;
	}

	public void setPrivateToken(String privateToken) {
		this.privateToken = privateToken;
	}

	public static Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}
