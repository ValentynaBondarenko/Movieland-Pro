package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
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
@SpringBootTest()
class MovieServiceImplTest extends AbstractITest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    @Test
    @DataSet(value = "datasets/movie/dataset_add_movie.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/movie/dataset_add_movie.yml")
    void testFindAllMovies() {
        List<ResponseMovieDTO> movies = movieService.findAllMovies();

        assertNotNull(movies);

        assertEquals(25, movies.size());
        ResponseMovieDTO firstMovie = movies.get(0);
        ResponseMovieDTO testMovie = testDTO();
        assertEquals(testMovie.getId(), firstMovie.getId());
        assertEquals(testMovie.getNameUkrainian(), firstMovie.getNameUkrainian());
        assertEquals(testMovie.getNameNative(), firstMovie.getNameNative());
    }

    @Test
    @DataSet(value = "datasets/movie/dataset_add_movie.yml", cleanBefore = true, cleanAfter = true)
    void testRandomMovies() {
        List<ResponseMovieDTO> movies = movieService.getRandomMovies();

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
    @DataSet(value = "datasets/movie/dataset_movies.yml", cleanBefore = true, cleanAfter = true)
    void testGetMoviesByGenre() {
        int genreId = 1;
        List<ResponseMovieDTO> moviesByGenre = movieService.getMoviesByGenre(genreId);

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
    void testFindAllMoviesWithSortingDescendingByRatingAndAscendingByPrice() {
        List<Movie> expectedMovies = movieRepository.findAllByOrderByRatingDescPriceAsc();
        List<ResponseMovieDTO> actualMovies = movieService.findAllMoviesWithSorting("desc", "asc");
        assertMoviesListEquals(expectedMovies, actualMovies);
    }

    @Test
    void testFindAllMoviesWithSortingAscendingByRatingAndDescendingByPrice() {
        List<Movie> expectedMovies = movieRepository.findAllByOrderByRatingAscPriceDesc();
        List<ResponseMovieDTO> actualMovies = movieService.findAllMoviesWithSorting("asc", "desc");
        assertMoviesListEquals(expectedMovies, actualMovies);
    }

    private void assertMoviesListEquals(List<Movie> expectedMovies, List<ResponseMovieDTO> actualMovies) {
        assertNotNull(actualMovies);
        assertEquals(expectedMovies.size(), actualMovies.size());

        for (int i = 0; i < expectedMovies.size(); i++) {
            Movie expectedMovie = expectedMovies.get(i);
            ResponseMovieDTO actualMovieDTO = actualMovies.get(i);
            Movie actualMovie = movieMapper.toMovie(actualMovieDTO);

            assertEquals(expectedMovie, actualMovie);
        }
    }

    private void assertMoviesAreEqual(Movie expectedMovie, ResponseMovieDTO actualMovie) {
        assertEquals(Optional.of(expectedMovie.getId()), actualMovie.getId());
        assertEquals(expectedMovie.getNameUkrainian(), actualMovie.getNameUkrainian());
        assertEquals(expectedMovie.getNameNative(), actualMovie.getNameNative());
    }

    private ResponseMovieDTO testDTO() {
        ResponseMovieDTO movieDTO = new ResponseMovieDTO();
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
