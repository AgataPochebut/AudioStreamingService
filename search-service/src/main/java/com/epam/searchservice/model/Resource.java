package com.epam.searchservice.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Resource extends BaseEntity {

    private String name;

    private Long size;

    @Column(unique = true)
    private String checksum;

//    @Enumerated(EnumType.STRING)
//    private StorageTypes storageType;
//
//    private String parent;
//
//    private String path;

}
