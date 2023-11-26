package com.bondarenko.movieland.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @Column(name = "genre_id")
    private int id;

    @Column(name = "genre_name")
    private String name;

}
