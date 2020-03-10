package com.epam.userservice.repository;

import com.epam.userservice.model.Playlist;
import com.epam.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
