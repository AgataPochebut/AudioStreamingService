package com.it.songservice.service.repository;

import com.it.songservice.dto.request.AlbumGetRequestDto;
import com.it.songservice.model.Album;
import com.it.songservice.model.Genre;
import com.it.songservice.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlbumRepositoryServiceImpl extends GenericRepositoryServiceImpl<Album, Long> implements AlbumRepositoryService {

    @Autowired
    private AlbumRepository repository;

    @Override
    public Album findByName(String s) {
        return repository.findByName(s).orElse(null);
    }

    @Override
    public List<Album> findAll(AlbumGetRequestDto requestDto) {
        Specification<Album> specification = Specification.where(null);

        if (requestDto.getName()!=null)
            specification = specification.and(hasName(requestDto.getName()));

        if (requestDto.getYear()!=0)
            specification = specification.and(hasYear(requestDto.getYear()));

        if (requestDto.getArtists()!=null) {
            Set<Long> set = Arrays.stream((requestDto.getArtists()).split(","))
                    .map(s -> Long.parseLong(s))
                    .collect(Collectors.toSet());
            specification = specification.and(hasArtists(set));
        }

        if (requestDto.getGenres()!=null) {
            Set<Long> set = Arrays.stream((requestDto.getGenres()).split(","))
                    .map(s -> Long.parseLong(s))
                    .collect(Collectors.toSet());
            specification = specification.and(hasGenres(set));
        }

        return repository.findAll(specification);
    }

    private Specification<Album> hasName(String name){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");
            return predicate;
        };
    }

    private Specification<Album> hasYear(Integer year){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("year"), year);
            return predicate;
        };
    }

    private Specification<Album> hasArtists(Set<Long> artists){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<Album, Genre> join = root.join("artists");
            return criteriaBuilder.in(join.get("id")).value(artists);
        };
    }

    private Specification<Album> hasGenres(Set<Long> genres){
        return (root, criteriaQuery, criteriaBuilder) ->
        {
            Join<Album, Genre> join = root.join("genres");
            return criteriaBuilder.in(join.get("id")).value(genres);
        };
    }

}
