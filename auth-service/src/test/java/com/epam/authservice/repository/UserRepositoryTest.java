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
@Sql(scripts = "/insert_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        User user = repository.findById(1L).orElse(null);
        assertThat(user).isNotNull();
    }

    @Test
    void findByAccount() {
        User user = repository.findByAccount("test").orElse(null);
        assertThat(user).isNotNull();
    }

    @Test
    void saveShouldReturnErrorAccountNotUnique() throws Exception {
        User user = new User();
        user.setAccount("test");
        user.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> repository.save(user));
    }

    @Test
    void saveShouldReturnErrorAccountNotNull() throws Exception {
        User user = new User();
        user.setAccount(null);
        user.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(user));
    }

    @Test
    void saveShouldReturnErrorAccountNotEmpty() throws Exception {
        User user = new User();
        user.setAccount("");
        user.setRoles(Set.of(Role.USER));
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(user));
    }

    @Test
    void saveShouldReturnErrorRolesNotNull() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(null);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(user));
    }

    @Test
    void saveShouldReturnErrorRolesNotEmpty() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(Set.of());
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(user));
    }

    @Test
    void save() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(Set.of(Role.USER));
        repository.save(user);

        User user1 = repository.findByAccount("test_new").orElse(null);
        assertThat(user1).isNotNull();
        assertThat(user1.getAccount()).isEqualTo(user.getAccount());
        assertThat(user1.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    void update() throws Exception {
        User user = new User();
        user.setAccount("test_new");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        user.setId(1L);
        repository.save(user);

        User user1 = repository.findById(1L).orElse(null);
        assertThat(user1).isNotNull();
        assertThat(user1).isEqualTo(user);
    }

    @Test
    void deleteById() {
        repository.deleteById(1L);

        User user1 = repository.findById(1L).orElse(null);
        assertThat(user1).isNull();
    }
}