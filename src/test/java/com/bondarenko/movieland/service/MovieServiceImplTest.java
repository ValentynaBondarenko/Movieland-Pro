package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.service.movie.MovieService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.bondarenko.listener.*;
import jakarta.validation.Valid;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DBRider
@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
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

        List<ResponseMovie> movies = movieService.findAll(null);

        assertNotNull(movies);

        assertEquals(25, movies.size());
        ResponseMovie firstMovie = movies.get(0);
        ResponseMovie testMovie = testDTO();
        assertEquals(testMovie.getId(), firstMovie.getId());
        assertEquals(testMovie.getNameUkrainian(), firstMovie.getNameUkrainian());
        assertEquals(testMovie.getNameNative(), firstMovie.getNameNative());
        DataSourceListener.assertSelectCount(1);

    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_movies.yml")
    void testRandomMovies() {
        List<ResponseMovie> movies = movieService.getRandomMovies();

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
        int genreId = 1;
        List<ResponseMovie> moviesByGenre = movieService.getMoviesByGenre(genreId);

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
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(null)
                .ratingDirection(MovieSortCriteria.RatingDirectionEnum.ASC);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAll(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(7.6, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(8.9, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    @ExpectedDataSet(value = "datasets/movie/expected_movies.yml", ignoreCols = "id")
    void testFindAllWithSortingDescendingByDESCRating() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(null)
                .ratingDirection(MovieSortCriteria.RatingDirectionEnum.DESC);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAll(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(8.9, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(7.6, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByDESCPrice() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(MovieSortCriteria.PriceDirectionEnum.DESC)
                .ratingDirection(null);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAll(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(200.6, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(100.0, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByASCPrice() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(MovieSortCriteria.PriceDirectionEnum.ASC)
                .ratingDirection(null);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAll(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(100.0, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(200.6, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_full_movies.yml")
    void findFullMovieByMovieId() {
        ResponseFullMovie fullMovie = movieService.getMovieById(1);

        assertNotNull(fullMovie);

        assertEquals(1, fullMovie.getId());
        assertEquals("Втеча з Шоушенка", fullMovie.getNameUkrainian());
        assertEquals("The Shawshank Redemption", fullMovie.getNameNative());
        assertEquals(1994, fullMovie.getYearOfRelease());
        assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", fullMovie.getDescription());
        assertEquals(8.9, fullMovie.getRating(), 0.001);
        assertEquals(123.45, fullMovie.getPrice(), 0.001);
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", fullMovie.getPicturePath());

        List<ResponseGenre> genres = fullMovie.getGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        ResponseGenre genre = genres.get(0);
        assertEquals(1, genre.getId());
        assertEquals("драма", genre.getName());

        List<ResponseCountry> countries = fullMovie.getCountries();
        assertNotNull(countries);
        assertEquals(1, countries.size());
        ResponseCountry country = countries.get(0);
        assertEquals(1, country.getId());
        assertEquals("США", country.getName());

        List<ResponseReview> reviews = fullMovie.getReviews();
        assertNotNull(reviews);
        assertEquals(2, reviews.size());

        ResponseReview review1 = reviews.get(0);
        assertEquals(1, review1.getId());
        ResponseUser user1 = review1.getUser();
        assertNotNull(user1);
        assertEquals(1, user1.getId());
        assertEquals("Дарлін Едвардс", user1.getNickname());
        assertEquals("Геніальний фільм! Дивишся і думаєш «Так не буває!», але пізніше розумієш, що саме так і має бути. Починаєш знову осмислювати значення фрази, яку постійно використовуєш у своєму житті, «Надія помирає останньою». Адже якщо ти не надієшся, то все в твоєму житті гасне, не залишається сенсу. Фільм наповнений безліччю правильних афоризмів. Я впевнена, що буду переглядати його сотні разів.", review1.getText());

        ResponseReview review2 = reviews.get(1);
        assertEquals(2, review2.getId());
        ResponseUser user2 = review2.getUser();
        assertNotNull(user2);
        assertEquals(2, user2.getId());
        assertEquals("Габріель Джексон", user2.getNickname());
        assertEquals("Кіно це, безумовно, «з відзнакою якості». Що ж до першого місця в рейтингу, то, думаю, тут мало місце було для виставлення «десяток» від більшості глядачів разом із надутими відгуками кінокритиків. Фільм атмосферний. Він драматичний. І, звісно, заслуговує на те, щоб знаходитися досить високо в світовому кінематографі.", review2.getText());

        DataSourceListener.assertSelectCount(4);
    }
    @Test
    @DataSet(value = "datasets/movie/dataset_add_movie.yml")
    @ExpectedDataSet(value = "datasets/movie/dataset_expected_add_movie.yml", ignoreCols = "id")
    void addNewMovie() {
        MovieRequest movieRequest = getMovieRequest();
        ResponseFullMovie savedMovie = movieService.saveMovie(movieRequest);
        assertNotNull(savedMovie);
        assertNotNull(savedMovie.getId());
        assertEquals(movieRequest.getNameUkrainian(), savedMovie.getNameUkrainian());
        assertEquals(movieRequest.getNameNative(), savedMovie.getNameNative());
        assertEquals(movieRequest.getYearOfRelease(), savedMovie.getYearOfRelease());
        assertEquals(movieRequest.getDescription(), savedMovie.getDescription());
        assertEquals(movieRequest.getPrice(), savedMovie.getPrice());
        assertEquals(movieRequest.getPicturePath(), savedMovie.getPicturePath());

        List<ResponseGenre> savedGenres = savedMovie.getGenres();
        assertNotNull(savedGenres);
        assertEquals(2, savedGenres.size());

        List<ResponseCountry> savedCountries = savedMovie.getCountries();
        assertNotNull(savedCountries);
        assertEquals(2, savedCountries.size());

        DataSourceListener.assertInsertCount(1);
    }

    private MovieRequest getMovieRequest(){
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setNameUkrainian("Втеча з Шоушенка");
        movieRequest.setNameNative("The Shawshank Redemption");
        movieRequest.setYearOfRelease(1994);
        movieRequest.setRating(9.0);
        movieRequest.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movieRequest.setPrice(123.45);
        movieRequest.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        movieRequest.setCountries(Arrays.asList(1, 2));
        movieRequest.setGenres(Arrays.asList(1, 2, 3));
        return movieRequest;
    }

    private ResponseMovie testDTO() {
        ResponseMovie movieDTO = new ResponseMovie();
        movieDTO.setId(1);
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
