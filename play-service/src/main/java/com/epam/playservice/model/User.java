package com.epam.playservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="USERS")
@NoArgsConstructor
@Data
public class User extends BaseEntity {

}
