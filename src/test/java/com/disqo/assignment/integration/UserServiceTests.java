package com.disqo.assignment.integration;

import com.disqo.assignment.AssignmentApplicationTests;
import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.EntityNotFoundException;
import com.disqo.assignment.exception.FieldIncorrectException;
import com.disqo.assignment.repository.UserRepository;
import com.disqo.assignment.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTests extends AssignmentApplicationTests {
  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @AfterEach
  void afterEach() {
    userRepository.deleteAll();
  }

  @Test
  void saveUserTest() {
    String password = "passwordTest";
    User user = User.builder()
        .email("test.test@test.ts")
        .password(password)
        .build();
    User persistedUser = userService.saveUser(user);
    Assertions.assertNotNull(persistedUser.getId());
    Assertions.assertEquals(persistedUser.getEmail(), user.getEmail());
    Assertions.assertTrue(passwordEncoder.matches(password, persistedUser.getPassword()));
  }

  @Test
  void saveUserTestWithEmailIncorrectResult() {
    User user = User.builder()
        .email("test.test")
        .password("passwordTest")
        .build();
    Assertions.assertThrows(FieldIncorrectException.class, () -> userService.saveUser(user));
  }

  @Test
  void saveUserTestWithPasswordIncorrectResult() {
    User user = User.builder()
        .email("test.test@test.ts")
        .password("1234567")
        .build();
    Assertions.assertThrows(FieldIncorrectException.class, () -> userService.saveUser(user));
  }

  @Test
  void getUserByIdTest() {
    String password = "passwordTest";
    User user = User.builder()
        .email("test.test@test.ts")
        .password(password)
        .build();
    user = userService.saveUser(user);
    String email = user.getEmail();
    User user1 = userService.getUser(user.getId());
    Assertions.assertEquals(email, user1.getEmail());
  }

  @Test
  void getUserByEmailTest() {
    String password = "passwordTest";
    User user = User.builder()
        .email("test.test@test.ts")
        .password(password)
        .build();
    user = userService.saveUser(user);
    User user1 = userService.getUserByEmail(user.getEmail());
    Assertions.assertEquals(user.getEmail(), user1.getEmail());
    Assertions.assertEquals(user.getId(), user1.getId());
  }

  @Test
  void deleteUserByIdTest() {
    String password = "passwordTest";
    User user = User.builder()
        .email("test.test@test.ts")
        .password(password)
        .build();
    User persistedUser = userService.saveUser(user);
    userService.deleteUser(user.getId());
    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUser(persistedUser.getId()));
  }
}
