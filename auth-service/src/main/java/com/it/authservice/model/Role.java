package com.it.authservice.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name="ROLES")
//public class Role extends BaseEntity {
//
//    private String name;
//
////    @Enumerated(EnumType.STRING)
////    @Column(name = "role")
////    private RoleEnum role;
//}

public enum Role {
    USER, ADMIN;

    public static Set<Role> defaultRoles = new HashSet<>(Arrays.asList(Role.USER));

}
