package com.epam.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends BaseEntity {

    private String name;

    private Long size;

    private String checksum;

//    @Enumerated(EnumType.STRING)
//    private StorageTypes storageType;
//
//    private String parent;
//
//    private String path;

}
