package com.epam.gateway.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    private Long id;

}
