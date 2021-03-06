package com.it.songservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="GENRES")
public class Genre extends BaseEntity {

    @NotNull
    @NotEmpty
    @Column(nullable = false, unique = true)
    String name;

}
