package com.epam.model;

import lombok.Builder;
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
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String parent;

    private String path;

    private Long size;

    private String checksum;

    enum StorageType {
        FS,
        S3
    }

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

}
