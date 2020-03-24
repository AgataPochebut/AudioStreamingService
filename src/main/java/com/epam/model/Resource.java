package com.epam.model;

import com.amazonaws.services.kafka.model.S3;
import lombok.*;
import org.springframework.core.io.InputStreamSource;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;

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

    private String checksum;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

}
