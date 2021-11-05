package com.disqo.assignment.config.security;

import com.disqo.assignment.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final JwtTokenFilter jwtTokenFilter;
  private final ServerAuthenticationSuccessHandler authenticationSuccessHandler;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf()
        .disable()
        .cors()
        .and()
        .authenticationManager(authenticationManager())
        .authorizeExchange()
        .anyExchange()
        .authenticated()
        .and()
        .formLogin()
        .securityContextRepository(securityContextRepository())
        .authenticationSuccessHandler(authenticationSuccessHandler)
        .loginPage("/login")
        .and()
        .addFilterBefore(jwtTokenFilter, SecurityWebFiltersOrder.FORM_LOGIN)
        .build();
  }

  @Bean
  public ServerSecurityContextRepository securityContextRepository() {
    return NoOpServerSecurityContextRepository.getInstance();
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager() {
    UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authenticationManager.setPasswordEncoder(passwordEncoder);
    authenticationManager.setUserDetailsPasswordService(userDetailsService);
    return authenticationManager;
  }


}
