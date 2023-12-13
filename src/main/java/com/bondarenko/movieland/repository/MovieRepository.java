package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenres_Id(int genre);

    List<Movie> findAllByOrderByRatingDescPriceAsc();

    List<Movie> findAllByOrderByRatingDescPriceDesc();

    List<Movie> findAllByOrderByRatingDesc();

    List<Movie> findAllByOrderByRatingAscPriceAsc();

    List<Movie> findAllByOrderByRatingAscPriceDesc();

    List<Movie> findAllByOrderByRatingAsc();
}
