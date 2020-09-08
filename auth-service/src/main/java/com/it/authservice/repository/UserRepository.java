package com.it.authservice.repository;

import com.it.authservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    Optional<User> findByAccount(String email);

}
