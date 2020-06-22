package com.epam.searchservice.model;

import lombok.Data;

@Data
public class S3Resource extends Resource {

    private String bucketName;

    private String keyName;

}
