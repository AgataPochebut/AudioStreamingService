package com.epam.authservice.model;

import com.epam.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ROLES")
public class Role extends BaseEntity {

    public static Set<Role> defaultRoles;

    private String name;
}

