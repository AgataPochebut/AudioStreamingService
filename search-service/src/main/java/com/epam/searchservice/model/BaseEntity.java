package com.epam.searchservice.model;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {

    @Id
    private Long id;

}
