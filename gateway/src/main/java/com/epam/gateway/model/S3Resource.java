package com.epam.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class S3Resource extends Resource {

    private String bucketName;

    private String keyName;

}
