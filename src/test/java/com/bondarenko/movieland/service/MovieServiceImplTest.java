package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import com.bondarenko.movieland.service.movie.MovieService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;

@DBRider
@SpringBootTest
class MovieServiceImplTest extends AbstractITest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

//    @Test
//    @DataSet(value = "datasets/movie/dataset_movies.yml")
//    @ExpectedDataSet(value = "datasets/movie/dataset_movies.yml")
//    void testFindAllMovies() {
//        List<ResponseMovie> movies = movieService.findAllMovies(null);
//
//        assertNotNull(movies);
//
//        assertEquals(25, movies.size());
//        ResponseMovie firstMovie = movies.get(0);
//        ResponseMovie testMovie = testDTO();
//        assertEquals(testMovie.getId(), firstMovie.getId());
//        assertEquals(testMovie.getNameUkrainian(), firstMovie.getNameUkrainian());
//        assertEquals(testMovie.getNameNative(), firstMovie.getNameNative());
//    }

    @Test
    @DataSet(value = "/datasets/movie/dataset_movies.yml")
    void testRandomMovies() {
        List<ResponseMovie> movies = movieService.getRandomMovies();

        Assertions.assertNotNull(movies);
        Assertions.assertEquals(3, movies.size());
        movies.forEach(movie ->
                assertAll(
                        () -> Assertions.assertNotNull(movie.getId()),
                        () -> Assertions.assertNotNull(movie.getNameUkrainian()),
                        () -> Assertions.assertNotNull(movie.getNameNative()),
                        () -> Assertions.assertNotNull(movie.getPrice()),
                        () -> Assertions.assertNotNull(movie.getDescription()),
                        () -> Assertions.assertNotNull(movie.getPoster()),
                        () -> Assertions.assertNotNull(movie.getYearOfRelease())
                )

        );
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testGetMoviesByGenre() {
        int genreId = 1;
        List<ResponseMovie> moviesByGenre = movieService.getMoviesByGenre(genreId);

        Assertions.assertNotNull(moviesByGenre);
        Assertions.assertEquals(1, moviesByGenre.size());
        moviesByGenre.forEach(movie ->
                assertAll(
                        () -> Assertions.assertEquals(1, movie.getId()),
                        () -> Assertions.assertEquals("Втеча з Шоушенка", movie.getNameUkrainian()),
                        () -> Assertions.assertEquals("The Shawshank Redemption", movie.getNameNative()),
                        () -> Assertions.assertEquals(123.45, movie.getPrice()),
                        () -> Assertions.assertEquals(8.9, movie.getRating()),
                        () -> Assertions.assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", movie.getDescription()),
                        () -> Assertions.assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", movie.getPoster()),
                        () -> Assertions.assertEquals(1994, movie.getYearOfRelease())
                )
        );
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByAscendingRating() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(null)
                .ratingDirection(MovieSortCriteria.RatingDirectionEnum.ASC);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAllMovies(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(7.6, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(8.9, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByDESCRating() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(null)
                .ratingDirection(MovieSortCriteria.RatingDirectionEnum.DESC);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAllMovies(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(8.9, Optional.ofNullable(movieDtoFirst.getRating()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(7.6, Optional.ofNullable(movieDtoLast.getRating()).orElse(0.0), 0.001);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByDESCPrice() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(MovieSortCriteria.PriceDirectionEnum.DESC)
                .ratingDirection(null);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAllMovies(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(200.6, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(100.0, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testFindAllMoviesWithSortingDescendingByASCPrice() {
        //prepare
        MovieSortCriteria movieSortCriteria = new MovieSortCriteria()
                .priceDirection(MovieSortCriteria.PriceDirectionEnum.ASC)
                .ratingDirection(null);

        //when
        List<ResponseMovie> allMoviesWithSorting = movieService.findAllMovies(movieSortCriteria);

        //then
        assertNotNull(allMoviesWithSorting);
        assertEquals(allMoviesWithSorting.size(), 25);

        ResponseMovie movieDtoFirst = allMoviesWithSorting.get(0);
        assertEquals(100.0, Optional.ofNullable(movieDtoFirst.getPrice()).orElse(0.0), 0.001);

        ResponseMovie movieDtoLast = allMoviesWithSorting.get(24);
        assertEquals(200.6, Optional.ofNullable(movieDtoLast.getPrice()).orElse(0.0), 0.001);
    }
@Test
@DataSet(value = "datasets/movie/datasets_full_movies.yml")
void findFullMovieByMovieId(){
    ResponseFullMovie movieById = movieService.getMovieById(1);

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
