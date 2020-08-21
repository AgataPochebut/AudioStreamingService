package com.epam.songservice.repository;

import com.epam.songservice.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Test
    void findAll() {
        List<Genre> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    void findById() {
        Genre obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void findByName() {
        Genre obj = repository.findByName("test").orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void saveShouldReturnErrorNameNotUnique() throws Exception {
        Genre obj = new Genre();
        obj.setName("test");
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorNameNotNull() throws Exception {
        Genre obj = new Genre();
        obj.setName(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorAccountNotEmpty() throws Exception {
        Genre obj = new Genre();
        obj.setName("");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void save() throws Exception {
        Genre obj = new Genre();
        obj.setName("test_new");
        repository.save(obj);
        Genre obj1 = repository.findByName("test_new").orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1.getName()).isEqualTo(obj.getName());
    }

    @Test
    void update() throws Exception {
        Genre obj = new Genre();
        obj.setName("test_new");
        obj.setId(1L);
        repository.save(obj);
        Genre obj1 = repository.findById(1L).orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);
        Genre obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNull();
    }

}