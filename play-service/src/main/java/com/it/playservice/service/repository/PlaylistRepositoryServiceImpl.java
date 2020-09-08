package com.it.playservice.service.repository;

import com.it.playservice.model.Playlist;
import com.it.playservice.repository.PlaylistRepository;
import com.it.playservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlaylistRepositoryServiceImpl extends GenericRepositoryServiceImpl<Playlist, Long> implements PlaylistRepositoryService {

    @Autowired
    private PlaylistRepository repository;

    @Autowired
    private UserService userService;

    @Override
    public List<Playlist> findAll() throws Exception {
        return repository.findAllByUser(userService.get());
    }

    @Override
    public Playlist findById(Long id) throws Exception {
        return repository.findByIdAndByUser(id, userService.get()).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public Playlist save(Playlist entity) throws Exception {
        entity.setUser(userService.get());
        return repository.save(entity);
    }

    //check id & user
    @Override
    public Playlist update(Playlist entity) {
        return repository.save(entity);
    }

    //check id & user
    @Override
    public void delete(Playlist entity) throws Exception {
        repository.delete(entity);

//        User user = userService.get();
//        if (user.getPlaylists().isEmpty()) userService.delete(user);
    }

    //check id & user
    @Override
    public void deleteById(Long id) throws Exception {
        repository.deleteById(id);

//        User user = userService.get();
//        if (user.getPlaylists().isEmpty()) userService.delete(user);
    }

//    @Override
//    public List<Playlist> findByUser(Long user_id) {
//        return repository.findByUser(user_id);
//    }
//
//    @Override
//    public List<Playlist> findBySong(Long song_id) {
//        return repository.findByUser(song_id);
//    }
}
