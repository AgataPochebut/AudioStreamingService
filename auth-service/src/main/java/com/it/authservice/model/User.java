package com.it.authservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USERS")
public class User extends BaseEntity {

    @NotNull
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String account;

    @NotNull
    @NotEmpty
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "User_Role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    private Set<Role> roles;
}
