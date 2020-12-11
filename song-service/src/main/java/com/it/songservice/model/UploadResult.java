package com.it.songservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="RESULTS")
public class UploadResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UploadStatus status;

    @Column(name = "resource_id")
    private Long resource;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "parent_child",
            joinColumns={@JoinColumn(name="child_id")},
            inverseJoinColumns={@JoinColumn(name="parent_id")})
    private UploadResult parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<UploadResult> details;

}
