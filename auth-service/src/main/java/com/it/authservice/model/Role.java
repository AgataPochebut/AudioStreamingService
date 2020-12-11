package com.it.authservice.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public enum Role {
    USER, ADMIN;

    public static Set<Role> defaultRoles = new HashSet<>(Arrays.asList(Role.USER));

}
