package com.disqo.assignment.validator;

import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.CustomException;
import com.disqo.assignment.exception.FieldIncorrectException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("userValidator")
public class UserValidator implements Validator<User> {
  @Value("${config.validation.password_min_length:8}")
  private int passwordMinLength;

  @Override
  public void validate(User user) throws CustomException {
    if (Objects.isNull(user.getPassword())) {
      throw new FieldIncorrectException("Password must not be null.");
    }
    if (Objects.isNull(user.getEmail())) {
      throw new FieldIncorrectException("Email must not be null.");
    }
    if (user.getPassword().length() < passwordMinLength) {
      throw new FieldIncorrectException("Password must be longer than or equals to " + passwordMinLength);
    }
    if (!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
      throw new FieldIncorrectException("Email is incorrect.");
    }
  }
}
