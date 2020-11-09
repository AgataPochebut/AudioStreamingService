package com.it.songservice.service.repository;

import com.it.songservice.dto.request.ArtistGetRequestDto;
import com.it.songservice.model.Artist;
import com.it.songservice.model.Genre;
import com.it.songservice.repository.ArtistRepository;
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
public class ArtistRepositoryServiceImpl extends GenericRepositoryServiceImpl<Artist, Long> implements ArtistRepositoryService {

    @Autowired
    private ArtistRepository repository;

    @Override
    public Artist findByName(String s) {
        return repository.findByName(s).orElse(null);
    }

    @Override
    public List<Artist> findAll(ArtistGetRequestDto requestDto) {
        Specification<Artist> specification = Specification.where(null);

        if (requestDto.getName()!=null)
            specification = specification.and(hasName(requestDto.getName()));

        if (requestDto.getGenres()!=null) {
            Set<Long> set = Arrays.stream((requestDto.getGenres()).split(","))
                    .map(s -> Long.parseLong(s))
                    .collect(Collectors.toSet());
            specification = specification.and(hasGenres(set));
        }

        return repository.findAll(specification);
    }

    @Override
    public List<Artist> findAll(Artist requestDto) {
        Specification<Artist> specification = Specification.where(null);

        if (requestDto.getName()!=null)
            specification = specification.and(hasName(requestDto.getName()));

        if (requestDto.getGenres()!=null) {
            Set<Long> set = requestDto.getGenres()
                    .stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toSet());
            specification = specification.and(hasGenres(set));
        }

        return repository.findAll(specification);
    }

    private Specification<Artist> hasName(String name){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");
            return predicate;
        };
    }

    private Specification<Artist> hasGenres(Set<Long> genres){
        return (root, criteriaQuery, criteriaBuilder) ->
        {
            Join<Artist, Genre> join = root.join("genres");
            return criteriaBuilder.in(join.get("id")).value(genres);
        };
    }

}
