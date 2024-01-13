package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
@Accessors(chain = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name_ukrainian")
    private String nameUkrainian;

    @Column(name = "name_native")
    private String nameNative;

    @Column(name = "year_of_release")
    private Integer yearOfRelease;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "poster")
    private String poster;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;


}
