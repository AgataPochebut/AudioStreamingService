package com.epam.songservice.model;

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
public class Resource {//extends BaseEntity {

    @Id
    private Long id;

    private String name;

    private Long size;

    @Column(unique = true)
    private String checksum;

    @Enumerated(EnumType.STRING)
    private StorageTypes storageType;

    private String path;

}
