package com.epam.playservice.repository;

import com.epam.playservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

}
