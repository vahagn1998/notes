package com.disqo.assignment.service;

import com.disqo.assignment.entity.Note;
import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.EntityNotFoundException;
import com.disqo.assignment.exception.FieldIncorrectException;
import com.disqo.assignment.repository.NoteRepository;
import com.disqo.assignment.validator.Validator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {
  private final NoteRepository noteRepository;
  private final UserService userService;
  private final Validator<Note> validator;

  public NoteServiceImpl(NoteRepository noteRepository,
      UserService userService, @Qualifier("noteValidator") Validator<Note> validator) {
    this.noteRepository = noteRepository;
    this.userService = userService;
    this.validator = validator;
  }

  @Override
  public Note getNote(long id) {
    return noteRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Note isn't found with id=" + id));
  }

  @Override
  public List<Note> getNotesByTitle(String title) {
    return noteRepository.findByTitle(title);
  }

  @Override
  public Note saveNote(Note note) {
    validator.validate(note);
    if (Objects.isNull(note.getId())) {
      User userByEmail = userService.getUserByEmail(note.getUser().getEmail());
      note.setUser(userByEmail);
    } else {
      Note persistedNote = getNote(note.getId());
      if (Objects.nonNull(note.getId()) && !Objects.equals(persistedNote.getUser(), note.getUser())) {
        throw new FieldIncorrectException("Note user must be the current authorized user.");
      }
      note.setUser(persistedNote.getUser());
    }
    return noteRepository.save(note);
  }

  @Override
  public void deleteNote(long id) {
    noteRepository.deleteById(id);
  }
}
