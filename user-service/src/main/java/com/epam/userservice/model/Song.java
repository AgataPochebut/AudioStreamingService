package com.epam.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="SONGS")
@NoArgsConstructor
@Data
public class Song {

    @Id
    private Long id;

}
