package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.security.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    User findByName(String name);

    User findByEmail(String email);

}
