package com.perc.challenge.taskmanager.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskManagerException extends RuntimeException {

    private String message;
    private int statusCode;

    public TaskManagerException(String message) {
        super(message);
    }

    public TaskManagerException(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
