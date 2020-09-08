package com.it.authservice.service.repository;

import com.it.authservice.model.Role;
import com.it.authservice.model.User;
import com.it.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserRepositoryServiceImpl repositoryService;

    @Test
    void findAll() {
        List<User> list = Arrays.asList();
        when(repository.findAll()).thenReturn(list);
        assertThat(repositoryService.findAll()).isEqualTo(list);
    }

    @Test
    void findById() {
        User obj = new User();
        when(repository.findById(any())).thenReturn(java.util.Optional.of(obj));
        assertThat(repositoryService.findById(1L)).isEqualTo(obj);
    }

    @Test
    void findByAccount() {
        User obj = new User();
        when(repository.findByAccount(any())).thenReturn(java.util.Optional.of(obj));
        assertThat(repositoryService.findByAccount("test")).isEqualTo(obj);
    }

    @Test
    void save() throws Exception {
        User obj = new User();
        obj.setAccount("test");
        obj.setRoles(Set.of(Role.USER));
        when(repository.save(any(User.class))).then(returnsFirstArg());
        assertThat(repositoryService.save(obj)).isEqualTo(obj);
    }

    @Test
    void update() throws Exception {
        User obj = new User();
        obj.setAccount("test");
        obj.setRoles(Set.of(Role.USER));
        obj.setId(1L);
        when(repository.save(any(User.class))).then(returnsFirstArg());
        assertThat(repositoryService.save(obj)).isEqualTo(obj);
    }

    @Test
    void deleteById() {
        when(repository.existsById(any())).thenReturn(true);
        doNothing().when(repository).deleteById(any());
        repositoryService.deleteById(1L);
    }

}