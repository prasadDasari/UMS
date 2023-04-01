package com.digicert.assesment.usermanagementservices.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digicert.assesment.usermanagementservices.entity.User;
import com.digicert.assesment.usermanagementservices.exception.ResourceNotFoundException;
import com.digicert.assesment.usermanagementservices.repository.UserRepository;


@RestController
@RequestMapping
public class UserController {
    private final UserRepository userRepository;
    
    /* Use constructor injection to inject the UserRepository into the UserController. */
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /* GET /users - returns a list of all users */
    @GetMapping(("/users"))
    public List<User> fetchAllUsers() {

        return userRepository.findAll();
    }
    
    /* GET /users/{id} - returns the user with the specified id */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable Long id) {
        
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value ="/users", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Validated @RequestBody User newUser) {
        
        User savedUser = userRepository.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

 
    /* PUT /users/{id} - updates the user with the specified id based on the request body */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Validated @RequestBody User userInfo) {
        
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setEmail(userInfo.getEmail());

        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
    
   /* DELETE /users/{id} - deletes the user with the specified id */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long userId) {
        
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        
        return ResponseEntity.ok().build();
    }
}

