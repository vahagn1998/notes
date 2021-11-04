package com.disqo.assignment.validator;

import com.disqo.assignment.exception.CustomException;

public interface Validator<T> {
  void validate(T t) throws CustomException;
}
