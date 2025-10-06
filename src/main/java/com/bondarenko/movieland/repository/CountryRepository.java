package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = """
            SELECT c.id, c.name
            FROM countries c
            JOIN movies_countries mc ON c.id = mc.country_id
            WHERE mc.movie_id = :movieId
            """, nativeQuery = true)
    List<Country> findByMovieId(@Param("movieId") Long movieId);

    List<Country> findByIdIn(List<Long> countryIds);
}
