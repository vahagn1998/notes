package com.disqo.assignment.controller.mapper;

import com.disqo.assignment.controller.dto.NoteDTO;
import com.disqo.assignment.entity.Note;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NoteMapper {
  public NoteDTO mapNoteToNoteDTO(Note note) {
    return NoteDTO.builder()
        .id(note.getId())
        .note(note.getNote())
        .title(note.getTitle())
        .user(UserMapper.mapUserToUserDTO(note.getUser()))
        .build();
  }

  public Note mapNoteDToToNote(NoteDTO noteDTO) {
    Note note = Note.builder()
        .note(noteDTO.getNote())
        .title(noteDTO.getTitle())
        .build();
    note.setId(noteDTO.getId());
    return note;
  }
}
