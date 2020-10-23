package com.it.storageservice.exception;

public class UploadException extends Exception {

    public UploadException(String errorMessage) {
        super(errorMessage);
    }

    public UploadException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
