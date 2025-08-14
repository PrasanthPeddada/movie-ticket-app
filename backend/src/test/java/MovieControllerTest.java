import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieticket.MovieTicketBookingApplication;
import com.movieticket.entity.Movie;
import com.movieticket.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
//@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional // Ensures each test rolls back changes automatically
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateMovie() throws Exception {
    String movieJson = "{"
            + "\"title\": \"Inception\","
            + "\"description\": \"Mind-bending thriller\","
            + "\"genre\": \"Sci-Fi\","
            + "\"language\": \"English\","
            + "\"durationMinutes\": 148"
            + "}";

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Movie created successfully"))
                .andExpect(jsonPath("$.movie.title").value("Inception"));
    }

    @Test
    void testGetAllMovies() throws Exception {
        

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void testGetMovieById_NotFound() throws Exception {
        mockMvc.perform(get("/movies/999"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").exists());
    }
}
