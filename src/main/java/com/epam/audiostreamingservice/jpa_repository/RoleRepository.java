package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.security.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericRepository<Role, Long> {
}
