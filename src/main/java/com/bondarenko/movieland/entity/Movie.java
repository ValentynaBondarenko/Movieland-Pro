package com.bondarenko.movieland.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_id_seq")
    @SequenceGenerator(name = "movies_id_seq", sequenceName = "movies_id_seq")
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
