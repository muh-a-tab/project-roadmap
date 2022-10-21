package com.project.roadmap.entity;

import com.project.roadmap.entity.Constants.TaskState;
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
public class Task {

    private String taskTitle;
    private TaskState taskState;
    private String url;

}
