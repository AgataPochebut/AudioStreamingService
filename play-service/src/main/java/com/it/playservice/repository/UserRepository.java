package com.it.playservice.repository;

import com.it.playservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {

}
