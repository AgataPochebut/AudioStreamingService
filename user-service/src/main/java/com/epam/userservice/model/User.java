package com.epam.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
//@Table
@NoArgsConstructor
@Data
public class User {

    @Id
    Long id;

}
