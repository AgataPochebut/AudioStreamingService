package com.epam.authservice.service;

import com.epam.authservice.model.User;

public interface UserService extends GenericService<User, Long> {

    User findByName(String name);
}
