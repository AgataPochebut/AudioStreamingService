package com.it.songservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum UploadStatus implements Serializable {
    STORED(1),
    PROCEEDED(2),
    UNPACKED(3),
    FAILED(4),
    FINISHED(5);

    private final int value;
}
