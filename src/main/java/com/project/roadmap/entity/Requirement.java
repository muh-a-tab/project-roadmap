package com.project.roadmap.entity;

import java.util.Date;
import java.util.List;

import com.project.roadmap.entity.Constants.RequirementState;
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
public class Requirement {

    private Long id;
    private String requirementTitle;
    private RequirementState requirementState;
    private List<Task> taskList;
    private Date startDate;
    private Date endDate;

}
