package com.epam.authservice.repository;

import com.epam.authservice.model.Role;
import com.epam.authservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Sql(scripts = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void findAll() {
        List<User> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    void findById() {
        User obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNotNull();
    }

    @Test
    void findByAccount() {
        User user = repository.findByAccount("test").orElse(null);
        assertThat(user).isNotNull();
    }

    @Test
    void saveShouldReturnErrorAccountNotUnique() throws Exception {
        User obj = new User();
        obj.setAccount("test");
        obj.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorAccountNotNull() throws Exception {
        User obj = new User();
        obj.setAccount(null);
        obj.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorAccountNotEmpty() throws Exception {
        User obj = new User();
        obj.setAccount("");
        obj.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorRolesNotNull() throws Exception {
        User obj = new User();
        obj.setAccount("test_new");
        obj.setRoles(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void saveShouldReturnErrorRolesNotEmpty() throws Exception {
        User obj = new User();
        obj.setAccount("test_new");
        obj.setRoles(Set.of());
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(obj));
    }

    @Test
    void save() throws Exception {
        User obj = new User();
        obj.setAccount("test_new");
        obj.setRoles(Set.of(Role.USER));
        repository.save(obj);

        User obj1 = repository.findByAccount("test_new").orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1.getAccount()).isEqualTo(obj.getAccount());
        assertThat(obj1.getRoles()).isEqualTo(obj.getRoles());
    }

    @Test
    void update() throws Exception {
        User obj = new User();
        obj.setAccount("test_new");
        obj.setRoles(Set.of(Role.USER, Role.ADMIN));
        obj.setId(1L);
        repository.save(obj);

        User obj1 = repository.findById(1L).orElse(null);
        assertThat(obj1).isNotNull();
        assertThat(obj1).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);

        User obj = repository.findById(1L).orElse(null);
        assertThat(obj).isNull();
    }
}