package com.epam.authservice.repository;

import com.epam.authservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    Optional<User> findByAccount(String email);

}
