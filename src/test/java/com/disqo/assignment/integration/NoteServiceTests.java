package com.disqo.assignment.integration;

import com.disqo.assignment.AssignmentApplicationTests;
import com.disqo.assignment.entity.Note;
import com.disqo.assignment.entity.User;
import com.disqo.assignment.exception.EntityNotFoundException;
import com.disqo.assignment.exception.FieldIncorrectException;
import com.disqo.assignment.repository.NoteRepository;
import com.disqo.assignment.repository.UserRepository;
import com.disqo.assignment.service.NoteService;
import com.disqo.assignment.service.UserService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class NoteServiceTests extends AssignmentApplicationTests {
  @Autowired
  NoteService noteService;

  @MockBean
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  NoteRepository noteRepository;

  @BeforeEach
  void beforeEach() {
    String password = "passwordTest";
    User user = User.builder()
        .email("test.test@test.ts")
        .password(password)
        .build();
    userRepository.save(user);
    Mockito.when(userService.getCurrentUser()).thenReturn(user);
  }

  @AfterEach
  void afterEach() {
    noteRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void saveNoteTest() {
    Note note = Note.builder()
        .title("title")
        .note("note")
        .build();
    Note persistedNote = noteService.saveNote(note);
    Assertions.assertNotNull(persistedNote.getId());
    Assertions.assertEquals(persistedNote.getTitle(), note.getTitle());
    Assertions.assertEquals(persistedNote.getNote(), note.getNote());
  }

  @Test
  void saveNoteTestWithTitleIncorrectResult() {
    Note note = Note.builder()
        .title("titlesdfasfasdffasf")
        .note("note")
        .build();
    Assertions.assertThrows(FieldIncorrectException.class, () -> noteService.saveNote(note));
  }

  @Test
  void saveNoteTestWithNoteIncorrectResult() {
    Note note = Note.builder()
        .title("title")
        .note("notedasdsadasfadsfasd")
        .build();
    Assertions.assertThrows(FieldIncorrectException.class, () -> noteService.saveNote(note));
  }

  @Test
  void getNoteByIdTest() {
    Note note = Note.builder()
        .title("title")
        .note("note")
        .build();
    note = noteService.saveNote(note);
    Note note1 = noteService.getNote(note.getId());
    Assertions.assertEquals(note.getTitle(), note1.getTitle());
  }

  @Test
  void getNotesByTitleTest() {
    Note note = Note.builder()
        .title("title")
        .note("note")
        .build();
    note = noteService.saveNote(note);
    List<Note> notesByTitle = noteService.getNotesByTitle(note.getTitle());
    Assertions.assertEquals(notesByTitle.size(), 1);
    Note note1 = notesByTitle.get(0);
    Assertions.assertEquals(note.getTitle(), note1.getTitle());
  }

  @Test
  void deleteNoteByIdTest() {
    Note note = Note.builder()
        .title("title")
        .note("note")
        .build();
    Note persistedNote = noteService.saveNote(note);
    noteService.deleteNote(note.getId());
    Assertions.assertThrows(EntityNotFoundException.class, () -> noteService.getNote(persistedNote.getId()));
  }
}
