package com.disqo.assignment.validator;

import com.disqo.assignment.entity.Note;
import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.CustomException;
import com.disqo.assignment.exception.FieldIncorrectException;
import com.disqo.assignment.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("noteValidator")
@RequiredArgsConstructor
public class NoteValidator implements Validator<Note> {
  private final UserService userService;

  @Value("${config.validation.title_max_length:50}")
  private int titleMaxLength;

  @Value("${config.validation.note_max_length:1000}")
  private int noteMaxLength;

  @Override
  public void validate(Note note) throws CustomException {
    if (Objects.isNull(note.getTitle())) {
      throw new FieldIncorrectException("Password must not be null.");
    }
    if (note.getTitle().length() > titleMaxLength) {
      throw new FieldIncorrectException("Title must be shorter than or equals to " + titleMaxLength);
    }
    if (note.getNote() != null && note.getNote().length() > noteMaxLength) {
      throw new FieldIncorrectException("Note must be shorter than or equals to " + noteMaxLength);
    }
  }
}
