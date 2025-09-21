package com.bondarenko.movieland.service.enrichment;


import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@DBRider
class ParallelEnrichmentServiceITest extends AbstractITest {
    @SpyBean
    private ParallelEnrichmentService enrichmentService;

    @Disabled
    @Test
    @DataSet("/datasets/movie/dataset_full_movies.yml")
    void testEnrichMovieTimeouts() {
        //prepare
        MovieRequest request = getMovieRequest();
        doAnswer(delayedAnswer())
                .when(enrichmentService).getCountriesTask(any(FullMovieResponse.class));

        doAnswer(delayedAnswer())
                .when(enrichmentService).getGenresTask(any(FullMovieResponse.class));

        doAnswer(delayedAnswer())
                .when(enrichmentService).getReviewsTask(any(FullMovieResponse.class));


        // when + timeout
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                enrichmentService.enrichMovie(any())
        );

        //then
        assertNotNull(request.getGenres());
        assertNotNull(request.getCountries());
        assertNotNull(request.getReview());

        assertFalse(request.getGenres().isEmpty());
        assertFalse(request.getCountries().isEmpty());
        assertFalse(request.getReview().isEmpty());

        assertEquals("Втеча з Шоушенка", request.getNameUkrainian());
        assertEquals("The Shawshank Redemption", request.getNameNative());
        assertEquals(1994, request.getYearOfRelease());
        assertEquals(123.45, request.getPrice());
        assertEquals(9.5, request.getRating());

        assertThat(request.getCountries())
                .extracting(CountryResponse::getName)
                .containsExactlyInAnyOrder("США", "Франція");

        assertThat(request.getGenres())
                .extracting(GenreResponse::getName)
                .containsExactlyInAnyOrder("Драма", "Кримінал", "Фентезі");

        assertThat(request.getReview())
                .extracting(r -> {
                    assert r.getUser() != null;
                    return r.getUser().getNickname();
                })
                .containsExactlyInAnyOrder(
                        "Дарлін Едвардс",
                        "Габріель Джексон",
                        "Рональд Рейнольдс"
                );

        assertThat(request.getReview()).allSatisfy(r -> {
            assertNotNull(r.getText());
            assertFalse(r.getText().isBlank());
        });
    }

    private Answer<Runnable> delayedAnswer() {
        return invocation -> {
            Runnable original = (Runnable) invocation.callRealMethod();
            return () -> {
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                original.run();
            };
        };
    }

    private MovieRequest getMovieRequest() {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setNameUkrainian("Втеча з Шоушенка");
        movieRequest.setNameNative("The Shawshank Redemption");
        movieRequest.setYearOfRelease(1994);
        movieRequest.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movieRequest.setPrice(123.45);
        movieRequest.setRating(9.5);
        movieRequest.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        List<CountryResponse> countries = new ArrayList<>();
        CountryResponse firstCountry = new CountryResponse();
        firstCountry.setId(1L);
        firstCountry.setName("США");
        countries.add(firstCountry);

        CountryResponse secondCountry = new CountryResponse();
        secondCountry.setId(2L);
        secondCountry.setName("Франція");
        countries.add(secondCountry);

        movieRequest.setCountries(countries);

        List<GenreResponse> genres = new ArrayList<>();

        GenreResponse firstGenre = new GenreResponse();
        firstGenre.setId(1L);
        firstGenre.setName("Драма");
        genres.add(firstGenre);

        GenreResponse secondGenre = new GenreResponse();
        secondGenre.setId(2L);
        secondGenre.setName("Кримінал");
        genres.add(secondGenre);

        GenreResponse thirdGenre = new GenreResponse();
        thirdGenre.setId(3L);
        thirdGenre.setName("Фентезі");
        genres.add(thirdGenre);

        movieRequest.setGenres(genres);

        ReviewResponse review1 = new ReviewResponse();
        review1.setId(1L);
        review1.setText("Геніальний фільм! Дивишся і думаєш «Так не буває!», але пізніше розумієш, що саме так і має бути...");

        ReviewResponse review2 = new ReviewResponse();
        review2.setId(2L);
        review2.setText("Кіно це, безумовно, «з відзнакою якості». Що ж до першого місця в рейтингу...");

        ReviewResponse review3 = new ReviewResponse();
        review3.setId(3L);
        review3.setText("Перестав дивуватися тому, що цей фільм займає постійно перше місце у всіляких кіно рейтингах...");

        List<ReviewResponse> reviews = new ArrayList<>(List.of(review1, review2, review3));
        movieRequest.setReview(reviews);

        return movieRequest;
    }

}