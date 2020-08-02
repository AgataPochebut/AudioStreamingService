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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class UserRepositoryServiceUnitTest {

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
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findByAccount() {
    }
}