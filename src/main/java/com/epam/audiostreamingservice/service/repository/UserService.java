package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.security.User;

public interface UserService extends GenericService<User, Long> {

    User findByName(String name);

    User findByEmail(String email);
}
