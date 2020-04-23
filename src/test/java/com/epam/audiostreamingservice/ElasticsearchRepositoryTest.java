package com.epam.audiostreamingservice;

import com.epam.audiostreamingservice.es_repository.SongESRepository;
import com.epam.audiostreamingservice.model.Song;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class ElasticsearchRepositoryTest {

    @Autowired
    private SongESRepository accountElasticRepository;

    private Song accountDocument;

    private ArrayList<Song> accountDocuments;

    @BeforeAll
    void initData() {
        accountDocument = new Song();

        accountDocuments = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            Song accountDocument = new Song();
            accountDocuments.add(accountDocument);
        }
    }

    @Test
    void save() {
        accountElasticRepository.save(accountDocument);
    }

    @Test
    void saveAll() {
        accountElasticRepository.saveAll(accountDocuments);
    }

    @Test
    void deleteById() {
        accountElasticRepository.deleteById(accountDocument.getId());
    }

    @Test
    void delete() {
        accountElasticRepository.delete(accountDocument);
    }

    @Test
    void deleteAll() {
        accountElasticRepository.deleteAll(accountDocuments);
    }

    @Test
    void deleteAll2() {
        accountElasticRepository.deleteAll();
    }

    @Test
    void findById() {
        System.err.println(accountElasticRepository.findById(accountDocument.getId()).orElse(null));
    }

}
