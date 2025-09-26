package com.bondarenko.movieland.service.enrichment;


import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.AbstractITest;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.doAnswer;

//@DBRider
class ParallelEnrichmentServiceITest extends AbstractITest {
    @SpyBean
    private ParallelEnrichmentService enrichmentService;

    @Test
        // @DataSet("/datasets/movie/dataset_full_movies.yml")
    void testEnrichMovieTimeouts() {
        //prepare
        MovieDto dto = getMovieResponse();
        doAnswer(delayedAnswer())
                .when(enrichmentService).getCountriesTask(dto);

        doAnswer(delayedAnswer())
                .when(enrichmentService).getGenresTask(dto);

        doAnswer(delayedAnswer())
                .when(enrichmentService).getReviewsTask(dto);


        // when + timeout
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> enrichmentService.enrichMovie(dto)
        );

        //then
        assertNotNull(dto.getGenres());
        assertNotNull(dto.getCountries());
        assertNotNull(dto.getReviews());
//
//        assertFalse(request.getGenres().isEmpty());
//        assertFalse(request.getCountries().isEmpty());
//        assertFalse(request.getReview().isEmpty());
//
//        assertEquals("Втеча з Шоушенка", request.getNameUkrainian());
//        assertEquals("The Shawshank Redemption", request.getNameNative());
//        assertEquals(1994, request.getYearOfRelease());
//        assertEquals(123.45, request.getPrice());
//        assertEquals(9.5, request.getRating());
//
//        assertThat(request.getCountries())
//                .extracting(CountryResponse::getName)
//                .containsExactlyInAnyOrder("США", "Франція");
//
//        assertThat(request.getGenres())
//                .extracting(GenreResponse::getName)
//                .containsExactlyInAnyOrder("Драма", "Кримінал", "Фентезі");

//        assertThat(request.getReview())
//                .extracting(r -> {
//                    assert r.getUser() != null;
//                    return r.getUser().getNickname();
//                })
//                .containsExactlyInAnyOrder(
//                        "Дарлін Едвардс",
//                        "Габріель Джексон",
//                        "Рональд Рейнольдс"
//                );
//
//        assertThat(request.getReview()).allSatisfy(r -> {
//            assertNotNull(r.getText());
//            assertFalse(r.getText().isBlank());
//        });
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

    private MovieDto getMovieResponse() {
        MovieDto dto = new MovieDto();
        dto.setNameUkrainian("Втеча з Шоушенка");
        dto.setNameNative("The Shawshank Redemption");
        dto.setYearOfRelease(1994);
        dto.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        dto.setPrice(123.45);
        dto.setRating(9.5);
        dto.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        List<CountryResponse> countries = new ArrayList<>();
        CountryResponse firstCountry = new CountryResponse();
        firstCountry.setId(1L);
        firstCountry.setName("США");
        countries.add(firstCountry);

        CountryResponse secondCountry = new CountryResponse();
        secondCountry.setId(2L);
        secondCountry.setName("Франція");
        countries.add(secondCountry);

        dto.setCountries(countries);

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

        dto.setGenres(genres);

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
        dto.setReviews(reviews);

        return dto;
    }

}