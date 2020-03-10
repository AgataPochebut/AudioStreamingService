package com.epam.userservice.repository;

import com.epam.userservice.model.Playlist;
import com.epam.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
