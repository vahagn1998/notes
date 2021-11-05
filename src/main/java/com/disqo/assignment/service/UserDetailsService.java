package com.disqo.assignment.service;

import com.disqo.assignment.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {
  private final UserService userService;

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return Mono.just(userService.getUserByEmail(username));
  }

  @Override
  public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
    return Mono.just(user)
        .map((userDetails) -> ((User) userDetails))
        .map((userDetails) -> {
          userDetails.setPassword(newPassword);
          return userService.saveUser(userDetails);
        });
  }
}
