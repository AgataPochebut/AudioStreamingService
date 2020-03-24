package com.epam.repository;

import com.epam.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@NoRepositoryBean
public interface GenericRepository<T,U> extends JpaRepository<T,U> {
}
