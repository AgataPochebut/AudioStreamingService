package com.epam.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="RESOURCES")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String parent;

    private String path;

    private Long size;

    @Column(unique = true)
    private String checksum;

    @Enumerated(EnumType.STRING)
    private StorageTypes storageTypes;

}
