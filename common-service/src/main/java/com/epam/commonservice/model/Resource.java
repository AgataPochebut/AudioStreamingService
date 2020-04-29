package com.epam.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="RESOURCES")
public class Resource extends BaseEntity {

    private String name;

    private String parent;

    private String path;

    private Long size;

    @Column(unique = true)
    private String checksum;

    @Enumerated(EnumType.STRING)
    private StorageTypes storageTypes;

}
