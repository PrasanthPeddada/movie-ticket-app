

import com.movieticket.dto.MovieWithShowsResponseDTO;
import com.movieticket.entity.Movie;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.repository.MovieRepository;
import com.movieticket.service.MovieService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
    movie.setId(1L);
    movie.setTitle("Interstellar");
    movie.setDescription("A space epic about love and survival.");
    movie.setGenre("Sci-Fi");
    movie.setLanguage("English");
    movie.setDurationMinutes(169);  
    movie.setPosterUrl("poster.jpg");
    movie.setTrailerUrl("trailer.mp4");
    movie.setRating(8.6);
    movie.setReleaseDate(LocalDateTime.of(2014, 11, 7, 0, 0));
    movie.setActive(true);
    movie.setShows(Collections.emptyList()); 
    }

    @Test
    void testGetAllActiveMovies() {
        when(movieRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(movie));

        List<Movie> result = movieService.getAllActiveMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
        verify(movieRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void testGetMovieById_Found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById(2L));
    }

    @Test
    void testCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie savedMovie = movieService.createMovie(movie);

        assertTrue(savedMovie.isActive());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testUpdateMovie() {
        Movie updateDetails = new Movie();
        updateDetails.setTitle("Inception");
        updateDetails.setDescription("Dream within a dream");
        updateDetails.setGenre("Sci-fi");
        updateDetails.setLanguage("English");
        updateDetails.setDurationMinutes(140);
        updateDetails.setPosterUrl("poster_url");
        updateDetails.setTrailerUrl("trailer_url");
        updateDetails.setRating(9.0);
        updateDetails.setReleaseDate(LocalDateTime.now());

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie updatedMovie = movieService.updateMovie(1L, updateDetails);

        assertEquals("Inception", updatedMovie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testDeleteMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(1L);

        assertFalse(movie.isActive());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testSearchMovies_ByGenre() {
        Movie m2 = new Movie();
        m2.setId(2L);
        m2.setTitle("Avengers");
        m2.setGenre("Action");
        m2.setLanguage("English");
        m2.setRating(8.5);
        m2.setActive(true);

        when(movieRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(movie, m2));

        List<Movie> result = movieService.searchMovies(null, "Sci-fi", null, null);

        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
    }

    @Test
    void testGetMovieWithShows() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieWithShowsResponseDTO dto = movieService.getMoviewWithShowsResponseDTO(1L);

        assertNotNull(dto);
        assertEquals("Interstellar", dto.getTitle());
    }
}
