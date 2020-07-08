package com.epam.songservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("S3")
//@Table(name="S3Resource")
public class S3Resource extends Resource {

    @NotNull
    @NotEmpty
    private String bucketName;

    @NotNull
    @NotEmpty
    private String keyName;

}
