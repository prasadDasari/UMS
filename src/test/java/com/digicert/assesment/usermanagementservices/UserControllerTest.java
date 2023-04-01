package com.digicert.assesment.usermanagementservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.digicert.assesment.usermanagementservices.controller.UserController;
import com.digicert.assesment.usermanagementservices.entity.User;
import com.digicert.assesment.usermanagementservices.exception.ResourceNotFoundException;
import com.digicert.assesment.usermanagementservices.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testFetchAllUsers() {
        List<User> users = List.of(
                new User(1L, "John", "Doe", "john.doe@example.com"),
                new User(2L, "Jane", "Doe", "jane.doe@example.com")
        );
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userController.fetchAllUsers();

        assertEquals(users.size(), result.size());
        assertEquals(users.get(0).getFirstName(), result.get(0).getFirstName());
    }

    @Test
    public void testFetchUserById() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> result = userController.fetchUserById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    public void testFetchUserByIdNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> result = userController.fetchUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(null, "John", "Doe", "john.doe@example.com");
        User savedUser = new User(1L, "John", "Doe", "john.doe@example.com");
        Mockito.when(userRepository.save(newUser)).thenReturn(savedUser);

        ResponseEntity<User> result = userController.createUser(newUser);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User(1L, "John", "Doe", "john.doe@example.com");
        User updatedUser = new User(1L, "Jane", "Doe", "jane.doe@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));
    
        ResponseEntity<User> result = userController.updateUser(1L, updatedUser);
    
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testUpdateUserNotFound() {
        User userInfo = new User(1L, "Jane", "Doe", "jane.doe@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.updateUser(1L, userInfo);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Void> result = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.deleteUser(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
