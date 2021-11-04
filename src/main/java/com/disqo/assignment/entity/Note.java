package com.disqo.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity {
  @Column(name = "title", nullable = false)
  @Max(value = 50)
  @NotNull
  private String title;

  @Column(name = "note")
  @Max(value = 1000)
  private String note;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
  private User user;
}
