package com.epam.authservice.repository;

import com.epam.authservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    User findByName(String name);

    User findByAccount(String email);

    Optional<User> findByClientAndExternalId(String client, String id);
}
