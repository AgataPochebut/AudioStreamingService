package com.epam.authservice.service;

import com.epam.authservice.model.User;
import com.epam.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public User findByAccount(String email) { return repository.findByAccount(email); }

}
