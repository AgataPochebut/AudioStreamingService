package com.epam.authservice.service.auth;

import com.epam.authservice.model.Role;
import com.epam.authservice.model.User;
import com.epam.authservice.service.repository.UserRepositoryService;
import com.epam.commonservice.model.auth.AuthUser;
import com.epam.commonservice.model.auth.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Override
    public AuthUser getUser(String s) throws Exception {
        Map<String, Object> attributes = getUserInfo(s);

        if (!attributes.containsKey("email")) {
            throw new Exception("Can not get email for token");
        }

        Set<Authority> roles = getUserAuthorities((String) attributes.get("email"));

        return AuthUser.builder()
                .authorities(roles)
                .attributes(attributes)
                .nameAttributeKey("name")
                .build();
    }

    private Map<String, Object> getUserInfo(String s){
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                .queryParam("access_token", s)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        Map<String, Object> attributes = (Map) response.getBody();
        return attributes;
    }

    private Set<Authority> getUserAuthorities(String s) throws Exception {
        User entity = userRepositoryService.findByAccount(s);
        if (entity == null) {
            entity = User.builder()
                    .account(s)
                    .roles(Role.defaultRoles)
                    .build();
            entity = userRepositoryService.save(entity);
        }
        return entity.getRoles().stream().map(i->new Authority(i.name())).collect(Collectors.toSet());
    }
}
