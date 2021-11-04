package com.disqo.assignment.exception.handler;

import com.disqo.assignment.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
@RequiredArgsConstructor
public class RestExceptionHandler implements WebExceptionHandler {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    if (ex instanceof CustomException) {
      log.info("Error occurred: {}", ex.getMessage());
      CustomException customException = (CustomException) ex;
      return handleException(exchange, customException);
    }
    log.error(ex.getMessage(), ex);
    return Mono.error(ex);
  }

  private Mono<Void> handleException(ServerWebExchange exchange, CustomException exception) {
    try {
      exchange.getResponse().setStatusCode(HttpStatus.valueOf(exception.getHttpStatus()));
      exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
      DefaultDataBuffer db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(exception));
      return exchange.getResponse().writeWith(Mono.just(db));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      return Mono.empty();
    }
  }

}
