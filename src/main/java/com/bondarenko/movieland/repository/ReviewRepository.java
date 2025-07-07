package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByMovieId(Long id);
}
