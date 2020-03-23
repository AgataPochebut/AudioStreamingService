package com.epam.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamSource;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;

@Entity
@Table(name="RESOURCES")
@NoArgsConstructor
@Data
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private Long size;

    private String checksum;

    enum StorageType {
        FILESYSTEM,
        S3
    }

    @Enumerated(EnumType.STRING)
    private StorageType storageType;
}
