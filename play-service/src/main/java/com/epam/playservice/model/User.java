package com.epam.playservice.model;

import com.epam.commonservice.model.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
@NoArgsConstructor
@Data
public class User extends BaseEntity {

}
