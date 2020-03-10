package com.epam.userservice.service;

import com.epam.songservice.model.Album;
import com.epam.userservice.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
}
