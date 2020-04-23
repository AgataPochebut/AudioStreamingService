package com.epam.userservice.repository;

import com.epam.userservice.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {
}
