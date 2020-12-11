package com.it.songservice.repository;

import com.it.songservice.model.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest(properties = "storage.type=S3")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/insert_songs.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/clean_artists.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SongRepositoryTest {

    @Autowired
    private SongRepository repository;

//    @Autowired
//    private ResourceLoader resourceLoader;
//
//    @Autowired
//    private ResourceStorageServiceManager resourceStorageServiceManager;
//
//    @BeforeEach
//    void before() throws Exception {
//        Resource source = resourceLoader.getResource("classpath:hurts - stay.mp3");
//        com.it.songservice.model.Resource resource = resourceStorageServiceManager.upload(source, source.getFilename());
//    }

    @Test
    void findAll() {
        List<Song> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    void findById() {
        Song obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void findByName() {
        Song obj = repository.findByName("test").orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void saveShouldReturnErrorNameNotNull() throws Exception {
        Song obj = new Song();
        obj.setName(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorNameNotEmpty() throws Exception {
        Song obj = new Song();
        obj.setName("");
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void save() throws Exception {
        Song obj = new Song();
        obj.setName("test_new");
        repository.save(obj);

        Song obj1 = repository.findByName("test_new").orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1.getName()).isEqualTo(obj.getName());
    }

    @Test
    void update() throws Exception {
        Song obj = repository.findById(1L).orElseThrow();
        obj.setName("test_upd");
        repository.save(obj);

        Song obj1 = repository.findById(1L).orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);
        Song obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNull();
    }

}