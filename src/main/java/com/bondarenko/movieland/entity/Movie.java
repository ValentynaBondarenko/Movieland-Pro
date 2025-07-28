package com.bondarenko.movieland.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
/**
 *  method chaining  for :
 *  Movie movie = new Movie().setTitle("Dune").setYear(2021);
 */
@Accessors(chain = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_seq")
    @SequenceGenerator(name = "movies_seq", sequenceName = "movies_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name_ukrainian", nullable = false)
    private String nameUkrainian;

    @Column(name = "name_native", nullable = false)
    private String nameNative;

    @Column(name = "year_of_release", nullable = false)
    private Integer yearOfRelease;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "rating", nullable = false)
    private BigDecimal rating;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "poster", columnDefinition = "text", nullable = false)
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

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", nameUkrainian='" + nameUkrainian + '\'' +
                ", nameNative='" + nameNative + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", poster='" + poster + '\'' +
                ", genres=" + genres +
                ", countries=" + countries +
                ", reviews=" + reviews +
                '}';
    }
}
