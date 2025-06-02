package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenresId(Long genre);

    @Query(value = """
            SELECT m.id, m.name_ukrainian, m.name_native, m.year_of_release, m.description, 
                    m.rating, m.price, m.poster 
                    FROM movies m 
                    ORDER BY RANDOM() 
                    LIMIT :limit 
            """, nativeQuery = true)
    List<Movie> findRandomMovies(Integer limit);

    Optional<Movie> getMovieById(Long movieId);
}
