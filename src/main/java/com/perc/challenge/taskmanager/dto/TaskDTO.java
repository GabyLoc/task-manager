package com.perc.challenge.taskmanager.dto;

import lombok.Data;

@Data
public class TaskDTO {

    private String name;
    private String description;
    private Boolean completed;
    private String dueDate;

}
