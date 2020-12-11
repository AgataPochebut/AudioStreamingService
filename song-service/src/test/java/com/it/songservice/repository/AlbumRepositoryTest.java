package com.it.songservice.repository;

import com.it.songservice.model.Album;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Sql(scripts = "/insert_albums.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_albums.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository repository;

    @Test
    void findAll() {
        List<Album> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    void findById() {
        Album obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void findByName() {
        Album obj = repository.findByName("test").orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void saveShouldReturnErrorNameNotNull() throws Exception {
        Album obj = new Album();
        obj.setName(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorNameNotEmpty() throws Exception {
        Album obj = new Album();
        obj.setName("");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void save() throws Exception {
        Album obj = new Album();
        obj.setName("test_new");
        repository.save(obj);

        Album obj1 = repository.findByName("test_new").orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1.getName()).isEqualTo(obj.getName());
    }

    @Test
    void update() throws Exception {
        Album obj = new Album();
        obj.setName("test_upd");
        obj.setId(1L);
        repository.save(obj);

        Album obj1 = repository.findById(1L).orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);
        Album obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNull();
    }

}