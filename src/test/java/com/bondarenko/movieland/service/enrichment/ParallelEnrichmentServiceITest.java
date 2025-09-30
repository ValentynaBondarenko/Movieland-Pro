package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@DBRider
class ParallelEnrichmentServiceITest extends AbstractITest {
    @Autowired
    private ParallelEnrichmentService enrichmentService;

    @Test
    @DataSet("/datasets/movie/dataset_full_movies.yml")
    void testEnrichMovieTimeouts() {
        //prepare
        Movie movie = getMovie();

        // when + timeout
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> enrichmentService.enrichMovie(movie)
        );

        //then enrich movie dto
        assertNotNull(movie.getGenres());
        assertNotNull(movie.getCountries());
        assertNotNull(movie.getReviews());

//        assertThat(movie.getCountries())
//                .extracting(Country::getName)
//                .containsExactlyInAnyOrder("США", "Франція");
//
//        assertThat(movie.getGenres())
//                .extracting(Genre::getName)
//                .containsExactlyInAnyOrder("Драма", "Кримінал", "Фентезі");

//        assertThat(movie.getReviews())
//                .extracting(review -> {
//                    assert review() != null;
//                    return review.getUser().getNickname();
//                })
//                .containsExactlyInAnyOrder(
//                        "Дарлін Едвардс",
//                        "Габріель Джексон",
//                        "Рональд Рейнольдс"
//                );

//        assertThat(movie.getReviews()).allSatisfy(review -> {
//            assertNotNull(review.getText());
//            assertFalse(review.getText().isBlank());
//        });
    }

    private Movie getMovie() {
        Movie movie = new Movie();
        movie.setNameUkrainian("Втеча з Шоушенка");
        movie.setNameNative("The Shawshank Redemption");
        movie.setYearOfRelease(1994);
        movie.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movie.setPrice(BigDecimal.valueOf(123.45));
        movie.setRating(BigDecimal.valueOf(9.5));
        movie.setPoster("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        List<Country> countries = new ArrayList<>();
        Country firstCountry = new Country();
        firstCountry.setId(1L);
        countries.add(firstCountry);

        Country secondCountry = new Country();
        secondCountry.setId(2L);
        countries.add(secondCountry);

        movie.setCountries(countries);

        List<Genre> genres = new ArrayList<>();

        Genre firstGenre = new Genre();
        firstGenre.setId(1L);
        genres.add(firstGenre);

        Genre secondGenre = new Genre();
        secondGenre.setId(2L);
        genres.add(secondGenre);

        Genre thirdGenre = new Genre();
        thirdGenre.setId(3L);
        genres.add(thirdGenre);

        movie.setGenres(genres);

        Review review1 = new Review();
        review1.setId(1L);

        Review review2 = new Review();
        review2.setId(2L);

        Review review3 = new Review();
        review3.setId(3L);

        List<Review> reviews = new ArrayList<>(List.of(review1, review2, review3));
        movie.setReviews(reviews);

        return movie;
    }

}