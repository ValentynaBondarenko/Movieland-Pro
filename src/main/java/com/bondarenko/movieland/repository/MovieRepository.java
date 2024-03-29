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

    List<Movie> findAll(Sort sort);

    @Query(value = "SELECT * FROM movies ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Movie> findRandomMovies(int limit);

    Optional<Movie> getMovieById(int movieId);
}
