package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "review", columnDefinition = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
/**
 * CREATE TABLE reviews
 * (
 * id       SERIAL PRIMARY KEY,
 * movie_id INT  NOT NULL,
 * user_id  INT  NOT NULL,
 * review   TEXT NOT NULL,
 * FOREIGN KEY (movie_id) REFERENCES movies (id),
 * FOREIGN KEY (user_id) REFERENCES users (id)
 * );
 */