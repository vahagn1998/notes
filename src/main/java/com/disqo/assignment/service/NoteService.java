package com.disqo.assignment.service;

import com.disqo.assignment.entity.Note;
import java.util.List;

public interface NoteService {
  Note getNote(long id);

  List<Note> getNotesByTitle(String title);

  Note saveNote(Note note);

  void deleteNote(long id);
}
