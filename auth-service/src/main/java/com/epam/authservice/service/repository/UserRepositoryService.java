package com.epam.authservice.service.repository;

import com.epam.authservice.model.User;

public interface UserRepositoryService extends GenericRepositoryService<User, Long> {

    User findByAccount(String email);
}
