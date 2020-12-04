package com.it.songservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SONGS")
public class Song extends BaseEntity {

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull
    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "album_id")
    private Album album;

}
