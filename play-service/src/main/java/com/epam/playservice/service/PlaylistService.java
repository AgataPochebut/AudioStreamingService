package com.epam.playservice.service;

import com.epam.playservice.model.Playlist;

import java.util.List;

public interface PlaylistService extends GenericService<Playlist, Long> {

    List<Playlist> findAll();

}
