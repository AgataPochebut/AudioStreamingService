package com.epam.playservice.service.repository;

import com.epam.playservice.model.User;
import com.epam.playservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRepositoryServiceImpl extends GenericRepositoryServiceImpl<User, Long> implements UserRepositoryService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }

}
