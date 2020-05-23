package com.epam.playservice.service;

import com.epam.playservice.model.Playlist;
import com.epam.playservice.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlaylistServiceImpl extends GenericServiceImpl<Playlist, Long> implements PlaylistService {

    @Autowired
    private PlaylistRepository repository;

    @Autowired
    private AuthUserService authUserService;

    @Override
    public List<Playlist> findAll() {
        //в репозитори нужно передавать юзера а не ид
        //юзера из бд
        //значит нужно получать юзера из репозитори по ид
        return repository.findAll(authUserService.getCurrentUser());
    }

    @Override
    public Playlist findById(Long id) {
        return repository.findById(authUserService.getCurrentUser().getId(), id).orElseThrow(() -> new RuntimeException());
    }

}
