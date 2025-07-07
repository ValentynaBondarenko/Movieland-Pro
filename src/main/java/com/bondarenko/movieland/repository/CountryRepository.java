package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "SELECT \n" +
            "    c.id, \n" +
            "    c.name\n" +
            "FROM countries c \n" +
            "JOIN movies_countries mc ON c.id = mc.country_id \n" +
            "WHERE mc.movie_id = :movieId", nativeQuery = true)
    Set<Country> findByMovieId(@Param("movieId") Long movieId);
}
