package com.it.authservice.service.repository;

import com.it.authservice.model.User;

public interface UserRepositoryService extends GenericRepositoryService<User, Long> {

    User findByAccount(String email);
}
