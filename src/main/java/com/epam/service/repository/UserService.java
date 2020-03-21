package com.epam.service.repository;

import com.epam.model.User;

public interface UserService extends GenericService<User, Long> {

    User findByName(String name);

    User findByEmail(String email);
}
