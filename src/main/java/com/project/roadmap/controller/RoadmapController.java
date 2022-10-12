package com.project.roadmap.controller;


import com.project.roadmap.service.IGitLabApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoadmapController {

    @Autowired
    IGitLabApiService gitLabApiService;

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("milestones", gitLabApiService.getProjectRoadmapStatus());
        model.addAttribute("appName", appName);
        return "roadmap";
    }
}
