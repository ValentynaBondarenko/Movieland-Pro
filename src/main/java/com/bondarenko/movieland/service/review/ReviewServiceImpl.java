package com.bondarenko.movieland.service.review;

import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public Set<Review> findByMovieId(Long id) {
        return reviewRepository.findByMovieId(id);
    }
}
