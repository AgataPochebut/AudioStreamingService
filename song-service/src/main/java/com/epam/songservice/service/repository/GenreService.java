package com.epam.songservice.service.repository;

import com.epam.songservice.model.Genre;

public interface GenreService extends GenericService<Genre, Long>  {

    Genre findByName(String s);

}
