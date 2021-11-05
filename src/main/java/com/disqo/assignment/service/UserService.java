package com.disqo.assignment.service;

import com.disqo.assignment.entity.User;

public interface UserService {
  User getUser(long id);

  User getUserByEmail(String email);

  User saveUser(User user);

  void deleteUser(long id);
}
