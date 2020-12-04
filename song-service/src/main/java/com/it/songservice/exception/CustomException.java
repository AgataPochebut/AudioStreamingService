package com.it.songservice.exception;

import lombok.Data;

import java.util.Set;

@Data
public class CustomException extends Exception {

    private Set<Exception> details;

    public CustomException(String errorMessage) {
        super(errorMessage);
    }

    public CustomException(String errorMessage, Set<Exception> details) {
        super(errorMessage);
        this.details = details;
    }

}
