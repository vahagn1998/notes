package com.disqo.assignment.controller;

import com.disqo.assignment.config.security.JwtTokenProvider;
import com.disqo.assignment.controller.dto.NoteDTO;
import com.disqo.assignment.controller.dto.UserDTO;
import com.disqo.assignment.controller.mapper.NoteMapper;
import com.disqo.assignment.entity.Note;
import com.disqo.assignment.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/note")
@AllArgsConstructor
public class NoteController {
  private final NoteService noteService;
  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/{id}")
  public Mono<NoteDTO> getNote(@PathVariable("id") long id) {
    return Mono.just(NoteMapper.mapNoteToNoteDTO(noteService.getNote(id)));
  }

  @GetMapping("/title/{title}")
  public Flux<NoteDTO> getNoteByTitle(@PathVariable("title") String title) {
    return Flux.just(noteService.getNotesByTitle(title).stream()
        .map(NoteMapper::mapNoteToNoteDTO)
        .toArray(NoteDTO[]::new));
  }

  @PostMapping()
  public Mono<NoteDTO> addNote(@RequestBody NoteDTO noteDTO, ServerWebExchange exchange) {
    String email = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(exchange));
    noteDTO.setUser(UserDTO.builder().email(email).build());
    Note note = NoteMapper.mapNoteDToToNote(noteDTO);
    note = noteService.saveNote(note);
    return Mono.just(NoteMapper.mapNoteToNoteDTO(note));
  }

  @PutMapping("/{id}")
  public Mono<NoteDTO> changeNote(@PathVariable("id") long id, @RequestBody NoteDTO noteDTO, ServerWebExchange exchange) {
    String email = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(exchange));
    noteDTO.setId(id);
    noteDTO.setUser(UserDTO.builder().email(email).build());
    Note note = NoteMapper.mapNoteDToToNote(noteDTO);
    note = noteService.saveNote(note);
    return Mono.just(NoteMapper.mapNoteToNoteDTO(note));
  }

  @DeleteMapping("/{id}")
  public Mono<Void> changeNote(@PathVariable("id") long id) {
    noteService.deleteNote(id);
    return Mono.empty();
  }

}
