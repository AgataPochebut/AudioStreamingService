package com.epam.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {//implements OAuth2User, OidcUser {

    private String account;

    private String name;

    private Set<Role> roles;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;//roles;
//    }
//
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return null;
//        //todo
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//
//    @Override
//    public Map<String, Object> getClaims() {
//        return null;
//    }
//
//    @Override
//    public OidcUserInfo getUserInfo() {
//        return null;
//    }
//
//    @Override
//    public OidcIdToken getIdToken() {
//        return null;
//    }
}
