package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(value = """
            SELECT g.id, g.name
            FROM genres g
            INNER JOIN movies_genres mg ON g.id = mg.genre_id
            WHERE mg.movie_id = :movieId
            """, nativeQuery = true)
    Set<Genre> findByMovieId(@Param("movieId") Long movieId);

}