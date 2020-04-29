package com.epam.authservice.repository;

import com.epam.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

    User findByName(String name);

    User findByEmail(String email);

}
