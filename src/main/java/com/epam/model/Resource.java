//package com.epam.model;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="RESOURCES")
//@NoArgsConstructor
//@Data
//public class Resource {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String path;
//
//    private Integer size;
//
//    private String checksum;
//
//    enum StorageType {
//        FILESYSTEM,
//        S3
//    }
//
//    @Enumerated(EnumType.STRING)
//    private StorageType storageType;
//}
