package com.epam.authservice.service.repository;

import com.epam.authservice.model.Role;
import com.epam.authservice.model.User;
import com.epam.authservice.repository.UserRepository;
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
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryServiceImpl userRepositoryService;

    @Test
    void findAll() {
        List<User> list = Arrays.asList();
        when(userRepository.findAll()).thenReturn(list);
        assertThat(userRepositoryService.findAll()).isEqualTo(list);
    }

    @Test
    void findById() {
        User user = new User();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        assertThat(userRepositoryService.findById(1L)).isEqualTo(user);
    }

    @Test
    void findByAccount() {
        User user = new User();
        when(userRepository.findByAccount(any())).thenReturn(java.util.Optional.of(user));
        assertThat(userRepositoryService.findByAccount("test")).isEqualTo(user);
    }

    @Test
    void save() throws Exception {
        User user = new User();
        user.setAccount("test");
        user.setRoles(Set.of(Role.USER));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        assertThat(userRepositoryService.save(user)).isEqualTo(user);
    }

    @Test
    void update() throws Exception {
        User user = new User();
        user.setAccount("test");
        user.setRoles(Set.of(Role.USER));
        user.setId(1L);
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        assertThat(userRepositoryService.save(user)).isEqualTo(user);
    }

    @Test
    void deleteById() {
        when(userRepository.existsById(any())).thenReturn(true);
        doNothing().when(userRepository).deleteById(any());
        userRepositoryService.deleteById(1L);
    }

}