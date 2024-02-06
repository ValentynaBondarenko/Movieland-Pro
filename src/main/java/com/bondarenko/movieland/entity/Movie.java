package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(name = "movies_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private List<Country> countries;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews;


}
