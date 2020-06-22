package com.epam.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Song extends BaseEntity {

   private String title;

    private Date year;

    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

//    @OneToOne
//    @JoinColumn(name = "resource_id")
//    private Resource resource;

}
