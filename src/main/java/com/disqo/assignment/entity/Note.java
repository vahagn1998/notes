package com.disqo.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Note extends BaseEntity {
  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "note")
  private String note;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
  private User user;
}
