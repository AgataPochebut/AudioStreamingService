package com.it.songservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {

    private Long id;

    private Set<String> messages;

    private UploadStatus status;

    private Set<UploadResult> details;

}
