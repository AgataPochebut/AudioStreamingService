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
        return repository.findAll(authUserService.getCurrentUser());
    }

    @Override
    public Playlist findById(Long id) {
        return repository.findById(authUserService.getCurrentUser(), id).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public Playlist save(Playlist entity) {
        entity.setUser(authUserService.getCurrentUser());
        return repository.save(entity);
    }

    @Override
    public Playlist update(Playlist entity) {
        if(entity.getUser().equals(authUserService.getCurrentUser())) {
            return repository.save(entity);
        }
        return entity;
    }

    @Override
    public void delete(Playlist entity) {
        if(entity.getUser().equals(authUserService.getCurrentUser())) {
            repository.delete(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        if(findById(id).getUser().equals(authUserService.getCurrentUser())) {
            repository.deleteById(id);
        }
    }

}
