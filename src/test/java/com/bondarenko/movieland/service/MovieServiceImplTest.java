package com.bondarenko.movieland.service;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.service.movie.MovieService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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
        UserResponse user1 = review1.getUser();
        assertNotNull(user1);
        assertEquals(1, user1.getId());
        assertEquals("Дарлін Едвардс", user1.getNickname());
        assertEquals("Геніальний фільм! Дивишся і думаєш «Так не буває!», але пізніше розумієш, що саме так і має бути. Починаєш знову осмислювати значення фрази, яку постійно використовуєш у своєму житті, «Надія помирає останньою». Адже якщо ти не надієшся, то все в твоєму житті гасне, не залишається сенсу. Фільм наповнений безліччю правильних афоризмів. Я впевнена, що буду переглядати його сотні разів.", review1.getText());

        ReviewResponse review2 = reviews.get(1);
        assertEquals(2, review2.getId());
        UserResponse user2 = review2.getUser();
        assertNotNull(user2);
        assertEquals(2, user2.getId());
        assertEquals("Габріель Джексон", user2.getNickname());
        assertEquals("Кіно це, безумовно, «з відзнакою якості». Що ж до першого місця в рейтингу, то, думаю, тут мало місце було для виставлення «десяток» від більшості глядачів разом із надутими відгуками кінокритиків. Фільм атмосферний. Він драматичний. І, звісно, заслуговує на те, щоб знаходитися досить високо в світовому кінематографі.", review2.getText());

        DataSourceListener.assertSelectCount(4);
    }

    @Test
    @DataSet("datasets/movie/dataset_before_add_movie.yml")
    @ExpectedDataSet(value = "datasets/movie/dataset_expected_add_movie.yml")
    void saveNewMovieToTheDatabase() {
        //prepare
        MovieRequest movieRequest = getMovieRequest();

        //when
        movieService.saveMovie(movieRequest);

    }

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
        assertEquals("Мої думки тихі", fullMovieResponse.getNameUkrainian());
        assertEquals("My Thoughts Are Silent", fullMovieResponse.getNameNative());
        assertEquals("Зазнавши безліч невдач у роботі та особистому житті, молодий звукорежисер Вадим отримує контракт на запис звуків закарпатських тварин...", fullMovieResponse.getDescription());
        assertEquals("https://upload.wikimedia.org/wikipedia/en/thumb/9/9b/My_Thoughts_Are_Silent_poster.jpg/250px-My_Thoughts_Are_Silent_poster.jpg", fullMovieResponse.getPicturePath());
        assertEquals(2019, fullMovieResponse.getYearOfRelease());
        assertEquals(160.0, fullMovieResponse.getPrice());
        assertEquals(7.8, fullMovieResponse.getRating());
        assertTrue(fullMovieResponse.getReviews().isEmpty());

        assertEquals("драма", fullMovieResponse.getGenres().getFirst().getName());
        assertEquals("комедія", fullMovieResponse.getGenres().getLast().getName());
        assertEquals(2, fullMovieResponse.getGenres().size());

        assertEquals("Україна", fullMovieResponse.getCountries().getFirst().getName());
        assertEquals(1, fullMovieResponse.getCountries().size());

    }

    private MovieRequest getMovieRequest() {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setNameUkrainian("Мої думки тихі");
        movieRequest.setNameNative("My Thoughts Are Silent");
        movieRequest.setYearOfRelease(2019);
        movieRequest.setDescription("Зазнавши безліч невдач у роботі та особистому житті, молодий звукорежисер Вадим отримує контракт на запис звуків закарпатських тварин для канадської компанії. Це може стати його шансом почати нове життя за кордоном, але в подорож разом із ним вирушає енергійна мама, що докорінно змінює всі плани.");
        movieRequest.setPrice(160.0);
        movieRequest.setRating(7.8);
        movieRequest.setPicturePath("https://upload.wikimedia.org/wikipedia/en/thumb/9/9b/My_Thoughts_Are_Silent_poster.jpg/250px-My_Thoughts_Are_Silent_poster.jpg");

        Set<CountryResponse> countries = new HashSet<>();
        CountryResponse firstCountry = new CountryResponse();
        firstCountry.setId(3L);
        firstCountry.setName("Україна");
        countries.add(firstCountry);

        movieRequest.setCountries(countries);

        Set<GenreResponse> genres = new HashSet<>();

        GenreResponse firstGenre = new GenreResponse();
        firstGenre.setId(1L);
        firstGenre.setName("Драма");
        genres.add(firstGenre);

        GenreResponse secondGenre = new GenreResponse();
        secondGenre.setId(4L);
        secondGenre.setName("Комедія");
        genres.add(secondGenre);

        movieRequest.setGenres(genres);
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
