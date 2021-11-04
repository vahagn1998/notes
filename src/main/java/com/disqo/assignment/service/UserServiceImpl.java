package com.disqo.assignment.service;

import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.EntityNotFoundException;
import com.disqo.assignment.repository.UserRepository;
import com.disqo.assignment.validator.Validator;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final Validator<User> validator;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, @Qualifier("userValidator") Validator<User> validator,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.validator = validator;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User getCurrentUser() {
    return null;
  }

  @Override
  public User getUser(long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User isn't found with id=" + id));
  }

  @Override
  public User getUserByEmail(@NotNull String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User saveUser(@Valid User user) {
    validator.validate(user);
    if (Objects.isNull(user.getId())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(long id) {
    userRepository.deleteById(id);
  }
}
