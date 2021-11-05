package com.disqo.assignment.config.security;

import com.disqo.assignment.controller.dto.JwtTokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
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
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    return Mono.just(authentication)
        .map(auth -> jwtTokenProvider.createToken(auth.getName(),
            auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())))
        .flatMap(token -> {
          ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
          try {
            DefaultDataBuffer db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(
                JwtTokenDTO.builder().token(token).build()));
            response.addCookie(ResponseCookie.from("jwt_token", token).build());
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(db));
          } catch (JsonProcessingException ignore) {
          }
          return Mono.empty();
        });
  }
}
