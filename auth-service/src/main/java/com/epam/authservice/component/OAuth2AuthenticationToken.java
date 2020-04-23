//package com.epam.authservice.component;
//
//import mx.wedevelop.oauth2.service.security.UserDetailsImpl;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
///**
// * Created by colorado on 9/03/17.
// */
//public class OAuth2AuthenticationToken extends AbstractAuthenticationToken {
//    /**
//     * serialVersionUID
//     */
//    private static final long serialVersionUID = 8254831403638075928L;
//
//    private UserDetailsImpl registeredUser;
//
//    public OAuth2AuthenticationToken(
//            Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//    }
//
//    /**
//     * @param authorities
//     * @param registeredUser
//     */
//    public OAuth2AuthenticationToken(UserDetailsImpl registeredUser) {
//        super(UserDetailsImpl.DEFAULT_ROLES);
//        this.registeredUser = registeredUser;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return "NOT_REQUIRED";
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return registeredUser;
//    }
//
//    /**
//     * @return the registeredUser
//     */
//    public UserDetailsImpl getUserDetail() {
//        return registeredUser;
//    }
//
//    /**
//     * @param registeredUser
//     *            the registeredUser to set
//     */
//    public void setUserDetail(UserDetailsImpl registeredUser) {
//        this.registeredUser = registeredUser;
//        setDetails(registeredUser);
//    }
//}