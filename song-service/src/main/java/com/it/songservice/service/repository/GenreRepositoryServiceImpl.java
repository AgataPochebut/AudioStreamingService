package com.it.songservice.service.repository;

import com.it.songservice.dto.request.GenreGetRequestDto;
import com.it.songservice.model.Genre;
import com.it.songservice.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
@Transactional
public class GenreRepositoryServiceImpl extends GenericRepositoryServiceImpl<Genre, Long> implements GenreRepositoryService {

    @Autowired
    private GenreRepository repository;

    @Override
    public Genre findByName(String s) {
        return repository.findByName(s).orElse(null);
    }

    @Override
    public List<Genre> findAll(GenreGetRequestDto requestDto) {
        Specification<Genre> specification = Specification.where(null);

        if (requestDto.getName() != null)
            specification = specification.and(hasName(requestDto.getName()));

        return repository.findAll(specification);
    }

    @Override
    public List<Genre> findAll(Genre requestDto) {
        Specification<Genre> specification = Specification.where(null);

        if (requestDto.getName() != null)
            specification = specification.and(hasName(requestDto.getName()));

        return repository.findAll(specification);
    }

    private Specification<Genre> hasName(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");
            return predicate;
        };
    }
}
