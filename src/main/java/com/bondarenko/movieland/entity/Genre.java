package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genre {
    @Id
    @GeneratedValue
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
