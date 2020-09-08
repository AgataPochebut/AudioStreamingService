package com.it.playservice.service.repository;

import com.it.playservice.model.User;
import com.it.playservice.repository.UserRepository;
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
