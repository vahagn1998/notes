package com.disqo.assignment.repository;

import com.disqo.assignment.entity.Note;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
  List<Note> findByTitle(String title);
}
