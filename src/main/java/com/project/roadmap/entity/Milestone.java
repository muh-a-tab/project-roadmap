package com.project.roadmap.entity;

import java.util.Date;
import java.util.List;

import com.project.roadmap.entity.Constants.MilestoneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Milestone {

    private Long id;
    private String milestoneTitle;
    private MilestoneState milestoneState;
    private List<Requirement> requirementList;
    private Date startDate;
    private Date endDate;

}
