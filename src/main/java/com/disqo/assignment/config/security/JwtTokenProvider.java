package com.disqo.assignment.config.security;

import com.disqo.assignment.enumiration.Roles;
import com.disqo.assignment.exception.AuthorizationException;
import com.disqo.assignment.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  public static final String BEARER_PREFIX = "Bearer ";

  private final UserService userService;

  @Value(value = "${config.jwt.secret}")
  private String secretKey;

  @Value(value = "${config.jwt.expiration_time:3600000}")
  private Long expirationTime;

  public String createToken(String username, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", roles);

    Date now = new Date();
    Date validity = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public Authentication getAuthentication(String token) {

    List<SimpleGrantedAuthority> roles = getRoles(token);

    if (roles.isEmpty()) {
      return null;
    }

    SimpleGrantedAuthority role = roles.get(0);
    UserDetails userDetails;
    if (Objects.equals(role.getAuthority(), Roles.ROLE_CUSTOMER.name())) {
      userDetails = userService.getUserByEmail(getUsername(token));
    } else {
      throw new AuthorizationException("Role " + role.getAuthority() + " doesn't exist.");
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  public List<SimpleGrantedAuthority> getRoles(String token) {
    return ((List<?>) Optional.ofNullable(getJwtBody(token)
        .get("roles")).orElseGet(ArrayList::new))
        .stream()
        .map(authority -> new SimpleGrantedAuthority((String) authority))
        .collect(Collectors.toList());
  }

  public String getUsername(String token) {
    return getJwtBody(token).getSubject();
  }

  public Claims getJwtBody(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public String resolveToken(ServerWebExchange serverWebExchange) {
    ServerHttpRequest request = serverWebExchange.getRequest();
    String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.replace(BEARER_PREFIX, "");
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (
        ExpiredJwtException ex) {
      throw new AuthorizationException("Token is expired.");
    } catch (MalformedJwtException | SignatureException ex) {
      throw new AuthorizationException("Token is malformed.");
    } catch (Exception ex) {
      throw new AuthorizationException("Token parsing error.");
    }
  }
}
