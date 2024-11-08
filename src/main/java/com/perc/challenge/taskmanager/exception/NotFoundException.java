package com.perc.challenge.taskmanager.exception;

import lombok.Data;

@Data
public class NotFoundException extends TaskManagerException {

    public NotFoundException(int code, String message) {
        super(code, message);
    }

}
