package com.perc.challenge.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.perc.challenge.taskmanager.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    @JsonProperty
    private Task task;
    @JsonProperty
    private Summary summary;

}
