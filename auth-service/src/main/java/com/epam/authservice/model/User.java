package com.epam.authservice.model;

import com.epam.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USERS")
public class User extends BaseEntity {

    private String account;

//    @NotNull
//    @NotEmpty
//    @ManyToMany
//    @JoinTable(name = "User_Role",
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id")})
//    private Set<Role> roles;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @JoinTable(name = "User_Role",
            joinColumns = {@JoinColumn(name = "user_id")})
    @Column(name = "role", nullable = false, unique = true)
    private Set<Role> roles;
}
