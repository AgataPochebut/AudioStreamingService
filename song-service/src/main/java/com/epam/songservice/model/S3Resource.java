package com.epam.songservice.model;

import com.epam.songservice.annotation.StorageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("S3")
@StorageType(StorageTypes.S3)
public class S3Resource extends Resource {

    @NotNull
    @NotEmpty
    private String bucketName;

}
