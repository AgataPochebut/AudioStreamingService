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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@MappedSuperclass
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
