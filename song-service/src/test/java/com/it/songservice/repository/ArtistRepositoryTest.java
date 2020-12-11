package com.it.songservice.repository;

import com.it.songservice.model.Artist;
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
@Sql(scripts = "/insert_artists.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean_artists.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository repository;

    @Test
    void findAll() {
        List<Artist> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    void findById() {
        Artist obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void findByName() {
        Artist obj = repository.findByName("test").orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void saveShouldReturnErrorNameNotUnique() throws Exception {
        Artist obj = new Artist();
        obj.setName("test");
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorNameNotNull() throws Exception {
        Artist obj = new Artist();
        obj.setName(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorNameNotEmpty() throws Exception {
        Artist obj = new Artist();
        obj.setName("");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void save() throws Exception {
        Artist obj = new Artist();
        obj.setName("test_new");
        repository.save(obj);

        Artist obj1 = repository.findByName("test_new").orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1.getName()).isEqualTo(obj.getName());
    }

    @Test
    void update() throws Exception {
        Artist obj = new Artist();
        obj.setName("test_upd");
        obj.setId(1L);
        repository.save(obj);

        Artist obj1 = repository.findById(1L).orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);
        Artist obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNull();
    }

}