package com.epam.authservice.service.repository;

import com.epam.authservice.model.User;

public interface UserService extends GenericService<User, Long> {

    User findByName(String name);

    User findByEmail(String email);
}
