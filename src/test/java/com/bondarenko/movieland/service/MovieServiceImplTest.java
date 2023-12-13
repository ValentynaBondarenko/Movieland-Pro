package com.bondarenko.movieland.service;

import com.bondarenko.movieland.MovielandApplication;
import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import com.bondarenko.movieland.service.movie.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = MovielandApplication.class)
class MovieServiceImplTest extends AbstractITest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    @Test
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
    void testRandomMovies() {
        List<ResponseMovieDTO> movies = movieService.getRandomMovies();

        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void testGetMoviesByGenre() {
        int genreId = 4;
        List<ResponseMovieDTO> moviesByGenre = movieService.getMoviesByGenre(genreId);

        assertNotNull(moviesByGenre);
        assertEquals(4, moviesByGenre.size());
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
        movieDTO.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        return movieDTO;
    }
}
