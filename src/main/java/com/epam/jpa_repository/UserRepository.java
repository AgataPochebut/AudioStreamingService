package com.epam.jpa_repository;

import com.epam.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    User findByName(String name);

    User findByEmail(String email);

}
