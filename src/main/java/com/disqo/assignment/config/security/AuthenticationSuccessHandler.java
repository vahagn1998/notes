package com.disqo.assignment.config.security;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    return Mono.just(authentication)
        .map(auth -> jwtTokenProvider.createToken(auth.getName(),
            auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())))
        .flatMap(token -> {
          ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
          response.addCookie(ResponseCookie.from("jwt_token", token).build());
          return Mono.empty();
        });
  }
}
