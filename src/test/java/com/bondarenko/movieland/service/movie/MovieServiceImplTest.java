package com.bondarenko.movieland.service.movie;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.AbstractITest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DBRider
class MovieServiceImplTest extends AbstractITest {
    @Autowired
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    @ExpectedDataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMovies() {

        List<MovieResponse> movies = movieService.findAll(null);

        assertNotNull(movies);

        assertEquals(25, movies.size());
        MovieResponse firstMovie = movies.get(0);
        MovieResponse testMovie = testDTO();
        assertEquals(testMovie.getId(), firstMovie.getId());
        assertEquals(testMovie.getNameUkrainian(), firstMovie.getNameUkrainian());
        assertEquals(testMovie.getNameNative(), firstMovie.getNameNative());
        DataSourceListener.assertSelectCount(1);

    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_movies.yml")
    void testRandomMovies() {
        List<MovieResponse> movies = movieService.getRandomMovies();

        assertNotNull(movies);
        assertEquals(3, movies.size());
        movies.forEach(movie ->
                assertAll(
                        () -> assertNotNull(movie.getId()),
                        () -> assertNotNull(movie.getNameUkrainian()),
                        () -> assertNotNull(movie.getNameNative()),
                        () -> assertNotNull(movie.getPrice()),
                        () -> assertNotNull(movie.getDescription()),
                        () -> assertNotNull(movie.getPoster()),
                        () -> assertNotNull(movie.getYearOfRelease())
                )
        );
        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testGetMoviesByGenre() {
        Long genreId = 1L;
        List<MovieResponse> moviesByGenre = movieService.getMoviesByGenre(genreId);

        assertNotNull(moviesByGenre);
        assertEquals(1, moviesByGenre.size());
        moviesByGenre.forEach(movie ->
                assertAll(
                        () -> assertEquals(1, movie.getId()),
                        () -> assertEquals("Втеча з Шоушенка", movie.getNameUkrainian()),
                        () -> assertEquals("The Shawshank Redemption", movie.getNameNative()),
                        () -> assertEquals(123.45, movie.getPrice()),
                        () -> assertEquals(8.9, movie.getRating()),
                        () -> assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", movie.getDescription()),
                        () -> assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", movie.getPoster()),
                        () -> assertEquals(1994, movie.getYearOfRelease())
                )
        );
        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllWithSortingDescendingByAscendingRating() {
        //prepare
        MovieSortRequest movieSortRequest = new MovieSortRequest()
                .priceDirection(null)
                .ratingDirection(MovieSortRequest.RatingDirectionEnum.ASC);

        //when
        List<MovieResponse> allMoviesWithSorting = movieService.findAll(movieSortRequest);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(25, allMoviesWithSorting.size());

        MovieResponse movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(7.6, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        MovieResponse movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(8.9, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllWithSortingDescendingByDESCRating() {
        //prepare
        MovieSortRequest movieSortRequest = new MovieSortRequest()
                .priceDirection(null)
                .ratingDirection(MovieSortRequest.RatingDirectionEnum.DESC);

        //when
        List<MovieResponse> allMoviesWithSorting = movieService.findAll(movieSortRequest);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(25, allMoviesWithSorting.size());

        MovieResponse movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(8.9, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        MovieResponse movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(7.6, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByDESCPrice() {
        //prepare
        MovieSortRequest movieSortRequest = new MovieSortRequest()
                .priceDirection(MovieSortRequest.PriceDirectionEnum.DESC)
                .ratingDirection(null);

        //when
        List<MovieResponse> allMoviesWithSorting = movieService.findAll(movieSortRequest);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(25, allMoviesWithSorting.size());

        MovieResponse movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(200.6, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        MovieResponse movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(100.0, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByASCPrice() {
        //prepare
        MovieSortRequest movieSortRequest = new MovieSortRequest()
                .priceDirection(MovieSortRequest.PriceDirectionEnum.ASC)
                .ratingDirection(null);

        //when
        List<MovieResponse> allMoviesWithSorting = movieService.findAll(movieSortRequest);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(25, allMoviesWithSorting.size());

        MovieResponse movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(100.0, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        MovieResponse movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(200.6, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_full_movies.yml")
    void findFullMovieByMovieId() {
        FullMovieResponse fullMovie = movieService.getMovieById(1L, null);

        assertNotNull(fullMovie);

        assertEquals(1, fullMovie.getId());
        assertEquals("Втеча з Шоушенка", fullMovie.getNameUkrainian());
        assertEquals("The Shawshank Redemption", fullMovie.getNameNative());
        assertEquals(1994, fullMovie.getYearOfRelease());
        assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", fullMovie.getDescription());
        assertEquals(8.9, fullMovie.getRating(), 0.001);
        assertEquals(123.45, fullMovie.getPrice(), 0.001);
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", fullMovie.getPicturePath());

        List<GenreResponse> genres = fullMovie.getGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        GenreResponse genre = genres.get(0);
        assertEquals(1, genre.getId());
        assertEquals("драма", genre.getName());

        List<CountryResponse> countries = fullMovie.getCountries();
        assertNotNull(countries);
        assertEquals(1, countries.size());
        CountryResponse country = countries.get(0);
        assertEquals(1, country.getId());
        assertEquals("США", country.getName());

        List<ReviewResponse> reviews = fullMovie.getReviews();
        assertNotNull(reviews);
        assertEquals(2, reviews.size());

        ReviewResponse review1 = reviews.get(0);
        assertEquals(1, review1.getId());

        UserIdResponse userFirst = review1.getUser();
        assertNotNull(userFirst);
        assertEquals(1, userFirst.getId());
        assertEquals("Дарлін Едвардс", userFirst.getNickname());
        assertEquals("Геніальний фільм! Дивишся і думаєш «Так не буває!», але пізніше розумієш, що саме так і має бути. Починаєш знову осмислювати значення фрази, яку постійно використовуєш у своєму житті, «Надія помирає останньою». Адже якщо ти не надієшся, то все в твоєму житті гасне, не залишається сенсу. Фільм наповнений безліччю правильних афоризмів. Я впевнена, що буду переглядати його сотні разів.", review1.getText());

        ReviewResponse review2 = reviews.get(1);
        assertEquals(2, review2.getId());
        UserIdResponse userSecond = review2.getUser();
        assertNotNull(userSecond);
        assertEquals("Габріель Джексон", userSecond.getNickname());
        assertEquals("Кіно це, безумовно, «з відзнакою якості». Що ж до першого місця в рейтингу, то, думаю, тут мало місце було для виставлення «десяток» від більшості глядачів разом із надутими відгуками кінокритиків. Фільм атмосферний. Він драматичний. І, звісно, заслуговує на те, щоб знаходитися досить високо в світовому кінематографі.", review2.getText());
        //DataSourceListener.assertSelectCount(10);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_before_add_movie.yml", cleanBefore = true)
    @ExpectedDataSet(value = "datasets/movie/dataset_expected_add_movie.yml")
    void saveNewMovieToTheDatabase() {
        //prepare
        MovieRequest movieRequest = getMovieRequest();

        //when
        movieService.saveMovie(movieRequest);
        FullMovieResponse created = movieService.getMovieById(2L, null);
        assertNotNull(created);

        assertEquals(2L, created.getId());
        assertEquals("Втеча з Шоушенка", created.getNameUkrainian());
        assertEquals("The Shawshank Redemption", created.getNameNative());
        assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", created.getDescription());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", created.getPicturePath());
        assertEquals(1994, created.getYearOfRelease());
        assertEquals(123.45, created.getPrice());
        assertEquals(9.5, created.getRating());

        assertTrue(created.getReviews().isEmpty());

        List<String> expectedGenres = List.of("драма", "кримінал", "трилер");
        List<String> actualGenres = created.getGenres().stream().map(GenreResponse::getName).toList();
        assertEquals(expectedGenres, actualGenres);

        List<String> expectedCountries = List.of("США", "Франція");
        List<String> actualCountries = created.getCountries().stream().map(CountryResponse::getName).toList();
        assertEquals(expectedCountries, actualCountries);

    }

    @Disabled
    @Test
    @DataSet("datasets/movie/dataset_before_update_movie.yml")
    @ExpectedDataSet(value = "datasets/movie/dataset_expected_update_movie.yml")
    void updateMovieInTheDatabase() {
        //prepare
        MovieRequest movieRequest = getMovieRequest();

        //when
        FullMovieResponse fullMovieResponse = movieService.updateMovie(1L, movieRequest);

        // then
        assertNotNull(fullMovieResponse);
        assertEquals(1, fullMovieResponse.getId());
        assertEquals("Втеча з Шоушенка", fullMovieResponse.getNameUkrainian());
        assertEquals("The Shawshank Redemption", fullMovieResponse.getNameNative());
        assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", fullMovieResponse.getDescription());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg",
                fullMovieResponse.getPicturePath());
        assertEquals(1994, fullMovieResponse.getYearOfRelease());
        assertEquals(123.45, fullMovieResponse.getPrice());
        assertEquals(9.5, fullMovieResponse.getRating());
        assertTrue(fullMovieResponse.getReviews().isEmpty());

        assertEquals("кримінал", fullMovieResponse.getGenres().get(0).getName());
        assertEquals("драма", fullMovieResponse.getGenres().get(1).getName());
        assertEquals("трилер", fullMovieResponse.getGenres().get(2).getName());
        assertEquals(3, fullMovieResponse.getGenres().size());

        assertEquals("США", fullMovieResponse.getCountries().getFirst().getName());
        assertEquals("Франція", fullMovieResponse.getCountries().getLast().getName());
        assertEquals(2, fullMovieResponse.getCountries().size());

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

        // Reviews
        List<ReviewResponse> reviews = new ArrayList<>();

        UserIdResponse userFirst = new UserIdResponse();
        userFirst.setId(1L);
        userFirst.setNickname("Дарлін Едвардс");
        ReviewResponse review1 = new ReviewResponse();
        review1.setId(1L);
        review1.setUser(userFirst);
        review1.setText("Вважаю, цей фільм має бути в колекції кожного поважного кіномана.");
        reviews.add(review1);

        UserIdResponse userSecond = new UserIdResponse();
        userSecond.setId(2L);
        userSecond.setNickname("Габріель Джексон");
        ReviewResponse review2 = new ReviewResponse();
        review2.setId(2L);
        review2.setUser(userSecond);
        review2.setText("Вічний шедевр світового кінематографа, який можна переглядати десятки разів.");
        reviews.add(review2);

        UserIdResponse userThird = new UserIdResponse();
        userThird.setId(3L);
        userThird.setNickname("Деріл Брайант");
        ReviewResponse review3 = new ReviewResponse();
        review3.setId(3L);
        review3.setUser(userThird);
        review3.setText("Фільм лише виграє від частого перегляду і завжди піднімає настрій.");
        reviews.add(review3);

        UserIdResponse userFourth = new UserIdResponse();
        userFourth.setId(4L);
        userFourth.setNickname("Ніл Паркер");
        ReviewResponse review4 = new ReviewResponse();
        review4.setId(4L);
        review4.setUser(userFourth);
        review4.setText("Безперечно культовий фільм, нереалістичний, але захопливий.");
        reviews.add(review4);

        movieRequest.setReview(reviews);

        return movieRequest;
    }

    private MovieResponse testDTO() {
        MovieResponse movieDTO = new MovieResponse();
        movieDTO.setId(1L);
        movieDTO.setNameUkrainian("Втеча з Шоушенка");
        movieDTO.setNameNative("The Shawshank Redemption");
        movieDTO.setYearOfRelease(1994);
        movieDTO.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві власної дружини і її коханця. Заарештований та висланий до вязниці під назвою Шоушенк, він зіткнувся з жорстокістю і беззаконням, що панує з обох сторін ґрат. Кожен, хто потрапляє в ці стіни, стає рабом до кінця свого життя. Але Енді, збройний розумом і доброю душею, відмовляється миритися з вироком долі і розробляє неймовірно сміливий план свого звільнення.");
        movieDTO.setRating(8.9);
        movieDTO.setPrice(123.45);
        movieDTO.setPoster("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        return movieDTO;
    }

}
