package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenresId(int genre);

    @Query(value = """
            SELECT m.id, m.name_ukrainian, m.name_native, m.year_of_release, m.description,
                   m.rating, m.price, m.poster, g.name AS genre
            FROM movies m
            JOIN movies_genres mg ON m.id = mg.movie_id
            JOIN genres g ON mg.genre_id = g.id
            ORDER BY RANDOM()
            LIMIT :limit
            """, nativeQuery = true)
    List<Movie> findRandomMovies(int limit);

    Optional<Movie> getMovieById(int movieId);
}
