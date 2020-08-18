package com.epam.authservice.service.repository;

import com.epam.authservice.model.User;
import com.epam.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRepositoryServiceImpl extends GenericRepositoryServiceImpl<User, Long> implements UserRepositoryService {

    private final UserRepository repository;

    public UserRepositoryServiceImpl(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }


    @Override
    public User findByAccount(String email) { return repository.findByAccount(email).orElse(null); }

}
