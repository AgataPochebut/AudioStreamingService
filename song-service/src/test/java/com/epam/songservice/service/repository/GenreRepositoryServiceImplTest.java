package com.epam.songservice.service.repository;

import com.epam.songservice.model.Genre;
import com.epam.songservice.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

//todo
//не понимаю смысла  теста, что вообще проверяется? наверное внутренность методов до вызова репозитория
//думаю лучше сделать инт без моков
@ExtendWith(MockitoExtension.class)
class GenreRepositoryServiceImplTest {

    @Mock
    private GenreRepository repository;

    @InjectMocks
    private GenreRepositoryServiceImpl repositoryService;

    @Test
    void findAll() {
        List<Genre> list = Arrays.asList();
        when(repository.findAll()).thenReturn(list);
        assertThat(repositoryService.findAll()).isEqualTo(list);
    }

    @Test
    void findById() {
        Genre obj = new Genre();
        when(repository.findById(any())).thenReturn(java.util.Optional.of(obj));
        assertThat(repositoryService.findById(1L)).isEqualTo(obj);
    }

    @Test
    void findByName() {
        Genre obj = new Genre();
        when(repository.findByName(any())).thenReturn(java.util.Optional.of(obj));
        assertThat(repositoryService.findByName("test")).isEqualTo(obj);
    }

    @Test
    void save() throws Exception {
        Genre obj = new Genre();
        obj.setName("test");
        when(repository.save(any(Genre.class))).then(returnsFirstArg());
        assertThat(repositoryService.save(obj)).isEqualTo(obj);
    }

    @Test
    void update() throws Exception {
        Genre obj = new Genre();
        obj.setName("test");
        obj.setId(1L);
        when(repository.save(any(Genre.class))).then(returnsFirstArg());
        assertThat(repositoryService.save(obj)).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        when(repository.existsById(any())).thenReturn(true);
        doNothing().when(repository).deleteById(any());
        repositoryService.deleteById(1L);
    }

}