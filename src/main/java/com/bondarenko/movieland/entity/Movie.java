package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movie")
@Accessors(chain = true)
public class Movie {
    @Id
    @Column(name = "movie_id")
    private int id;

    @Column(name = "name_ua")
    private String nameUkrainian;

    @Column(name = "name_native")
    private String nameNative;

    @Column(name = "release_year")
    private Integer yearOfRelease;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private double rating;

    @Column(name = "price")
    private double price;

    @Column(name = "poster")
    private String picturePath;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

}
