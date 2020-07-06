package com.epam.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("S3")
//@Table(name="S3Resource")
public class S3Resource extends Resource {

    private String bucketName;

    private String keyName;

}
