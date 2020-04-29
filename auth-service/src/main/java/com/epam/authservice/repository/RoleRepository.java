package com.epam.authservice.repository;

import com.epam.authservice.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericRepository<Role, Long> {
}
