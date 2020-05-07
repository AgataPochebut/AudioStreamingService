package com.epam.authservice.repository;

import com.epam.authservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    User findByName(String name);

    User findByAccount(String email);

}
