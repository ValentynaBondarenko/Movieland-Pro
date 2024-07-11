package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_id_seq")
    @SequenceGenerator(name = "genres_id_seq", sequenceName = "genres_id_seq")
    private int id;

    @Column(name = "name")
    private String name;

}
