package com.bondarenko.movieland.service.enrichment;


import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.repository.MovieRepository;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.AssertionsKt.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;


@DBRider
class ParallelEnrichmentServiceITest extends AbstractITest {
    @SpyBean
    private ParallelEnrichmentService enrichmentService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DataSet("/datasets/movie/dataset_full_movies.yml")
    void testEnrichMovieTimeouts() {
        MovieRequest request = getMovieRequest();
        doAnswer(invocation -> {
            Callable<List<CountryResponse>> original =
                    (Callable<List<CountryResponse>>) invocation.callRealMethod();

            return (Callable<List<CountryResponse>>) () -> {
                Thread.sleep(3000);
                return original.call();
            };
        }).when(enrichmentService).getGenresTask(any(MovieRequest.class));

        doAnswer(invocation -> {
            Callable<List<GenreResponse>> original =
                    (Callable<List<GenreResponse>>) invocation.callRealMethod();

            return (Callable<List<GenreResponse>>) () -> {
                Thread.sleep(3000);
                return original.call();
            };
        }).when(enrichmentService).getGenresTask(any(MovieRequest.class));

        doAnswer(invocation -> {
            Callable<List<ReviewResponse>> original =
                    (Callable<List<ReviewResponse>>) invocation.callRealMethod();

            return (Callable<List<ReviewResponse>>) () -> {
                Thread.sleep(3000);
                return original.call();
            };
        }).when(enrichmentService).getReviewsTask(any(MovieRequest.class));


        long start = System.currentTimeMillis();
        MovieRequest enriched = enrichmentService.enrichMovie(request);
        long end = System.currentTimeMillis();
        System.out.println("Total duration: " + (end - start) + " ms (" + ((end - start) / 1000.0) + " s)");
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                enrichmentService.enrichMovie(request)
        );

        assertNotNull(enriched.getGenres());
        assertNotNull(enriched.getCountries());
        assertNotNull(enriched.getReview());

        assertFalse(enriched.getGenres().isEmpty());
        assertFalse(enriched.getCountries().isEmpty());
        assertFalse(enriched.getReview().isEmpty());
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