package com.project.roadmap.controller;


import com.project.roadmap.service.GitLabApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class RoadmapController {

    private final GitLabApiService gitLabApiService;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("milestones", gitLabApiService.getProjectRoadmapStatus());
        model.addAttribute("incorrectRequirement", gitLabApiService.getUnplacedRequirement());
        model.addAttribute("incorrectTask", gitLabApiService.getUnplacedTask());
        model.addAttribute("appName", appName);
        return "roadmap";
    }
}
