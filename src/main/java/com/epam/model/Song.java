package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SONGS")
@Document(indexName = "techfou", type = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @NotEmpty
//    @Column(nullable = false)
    private String title;

//    @NotNull
//    @NotEmpty
//    @Column(nullable = false)
    private Date year;

//    @NotNull
//    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

//    @NotNull
//    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

//    @NotNull
//    @NotEmpty
    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

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
}
