package com.it.songservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum UploadStatus implements Serializable {
    PROCEEDED(0),
    FINISHED(1),
    FAILED(2);

    private final int value;
}
