package com.bondarenko.movieland.service.review;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DBRider
class ReviewServiceImplTest extends AbstractITest {
    @Autowired
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_full_movies.yml")
    void shouldFindAllReviews() {
        List<ReviewResponse> reviews = reviewService.findByIdIn(List.of(1L, 2L, 3L));

        assertEquals(3, reviews.size());

        ReviewResponse reviewFirst = reviews.getFirst();
        assertEquals(1L, reviewFirst.getId());
        assert reviewFirst.getUser() != null;
        assertEquals("Дарлін Едвардс", reviewFirst.getUser().getNickname());
        assert reviewFirst.getText() != null;
        assertTrue(reviewFirst.getText().contains("Геніальний фільм!"));

        ReviewResponse reviewSecond = reviews.get(1);
        assertEquals(2L, reviewSecond.getId());
        assert reviewSecond.getUser() != null;
        assertEquals("Габріель Джексон", reviewSecond.getUser().getNickname());
        assert reviewSecond.getText() != null;
        assertTrue(reviewSecond.getText().contains("Кіно це, безумовно"));

        ReviewResponse reviewThird = reviews.get(2);
        assertEquals(3L, reviewThird.getId());
        assert reviewThird.getUser() != null;
        assertEquals("Рональд Рейнольдс", reviewThird.getUser().getNickname());
        assert reviewThird.getText() != null;
        assertTrue(reviewThird.getText().contains("Перестав дивуватися"));
    }

}