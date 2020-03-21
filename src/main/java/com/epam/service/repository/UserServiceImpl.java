package com.epam.service.repository;

import com.epam.model.User;
import com.epam.repository.UserRepository;
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
    public User findByEmail(String email) { return repository.findByEmail(email); }

}
