package com.epam.playservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="SONGS")
@NoArgsConstructor
@Data
public class Song extends BaseEntity {

}
