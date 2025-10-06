package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        MovieRequest movie = getMovie();

        // when + timeout
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> enrichmentService.enrichMovie(new Movie())
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
//                .containsExactlyInAnyOrder("драма", "кримінал", "фентезі");

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

    private MovieRequest getMovie() {
        MovieRequest movie = new MovieRequest();
        movie.setNameUkrainian("Втеча з Шоушенка");
        movie.setNameNative("The Shawshank Redemption");
        movie.setYearOfRelease(1994);
        movie.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movie.setPrice(123.45);
        movie.setRating(9.5);
        movie.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        List<CountryResponse> countries = new ArrayList<>();
        CountryResponse firstCountry = new CountryResponse();
        firstCountry.setId(1L);
        countries.add(firstCountry);

        CountryResponse secondCountry = new CountryResponse();
        secondCountry.setId(2L);
        countries.add(secondCountry);

        movie.setCountries(countries);

        List<GenreResponse> genres = new ArrayList<>();

        GenreResponse firstGenre = new GenreResponse();
        firstGenre.setId(1L);
        genres.add(firstGenre);

        GenreResponse secondGenre = new GenreResponse();
        secondGenre.setId(2L);
        genres.add(secondGenre);

        GenreResponse thirdGenre = new GenreResponse();
        thirdGenre.setId(3L);
        genres.add(thirdGenre);

        movie.setGenres(genres);

        ReviewResponse review1 = new ReviewResponse();
        review1.setId(1L);

        ReviewResponse review2 = new ReviewResponse();
        review2.setId(2L);

        ReviewResponse review3 = new ReviewResponse();
        review3.setId(3L);

        List<ReviewResponse> reviews = new ArrayList<>(List.of(review1, review2, review3));
        movie.setReviews(reviews);

        return movie;
    }

}