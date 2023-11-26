package com.bondarenko.movieland.service;

import com.bondarenko.movieland.MovielandApplication;
import com.bondarenko.movieland.dto.RequestMovieDTO;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = MovielandApplication.class)
@Testcontainers
class MovieServiceImplTest extends AbstractITest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    @Test
    public void testFindAllMovies() {

        List<RequestMovieDTO> movies = movieService.findAllMovies();

        assertNotNull(movies);
        assertEquals(25, movies.size());
        RequestMovieDTO firstMovie = movies.get(0);
        RequestMovieDTO testMovie = testDTO();
        assertEquals(testMovie.getId(), firstMovie.getId());
        assertEquals(testMovie.getNameUkrainian(), firstMovie.getNameUkrainian());
        assertEquals(testMovie.getNameNative(), firstMovie.getNameNative());
    }
    @Test
    public void testRandomMovies() {
        List<RequestMovieDTO> movies = movieService.getRandomMovies();

        assertNotNull(movies);
        assertEquals(3, movies.size());
    }


    private RequestMovieDTO testDTO() {
        return RequestMovieDTO.builder()
                .id(1)
                .nameUkrainian("Втеча з Шоушенка")
                .nameNative("The Shawshank Redemption")
                .yearOfRelease(1994)
                .description("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві власної дружини і її коханця. Заарештований та висланий до вязниці під назвою Шоушенк, він зіткнувся з жорстокістю і беззаконням, що панує з обох сторін ґрат. Кожен, хто потрапляє в ці стіни, стає рабом до кінця свого життя. Але Енді, збройний розумом і доброю душею, відмовляється миритися з вироком долі і розробляє неймовірно сміливий план свого звільнення.")
                .rating(8.9)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();
    }
}