package br.com.accmd.easyFlix.application.movies;

import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyMovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final EasyMovieService movieService;
    private final MovieMapper movieMapper;

    @PostMapping
    public ResponseEntity save(
            @RequestParam("file")MultipartFile file,
            @RequestParam("movieTitle") String movieTitle,
            @RequestParam("duration")String duration,
            @RequestParam("country") String country,
            @RequestParam("movieGenre") MovieGenre movieGenre,
            @RequestParam("movieLink") String movieLink) throws IOException{
        log.info("Filme recebido - Título do filme: {}", movieTitle);

        EasyMovie easyMovie = movieMapper.mapToEasyMovie(file, movieTitle, duration, country, movieGenre, movieLink);
        EasyMovie savedMovie = movieService.save(easyMovie);
        URI movieURI = buildMovieURL(savedMovie);

        return ResponseEntity.created(movieURI).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMoviePoster(@PathVariable("id") String id) {
        var possibleMovie = movieService.getById(id);
        if (possibleMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var movie = possibleMovie.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Altere conforme necessário
        headers.setContentLength(movie.getFile().length);
        headers.setContentDispositionFormData("inline; filename=\"" + movie.getFileName() + "\"", movie.getFileName());

        return new ResponseEntity<>(movie.getFile(), headers, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<MovieDto>> search(
            @RequestParam(value = "movieGenre", required = false, defaultValue = "") String movieGenre,
            @RequestParam(value = "query", required = false) String query){

        var result = movieService.search(MovieGenre.ofName(movieGenre), query);

        var movies = result.stream().map(movie -> {
            var url = buildMovieURL(movie);
            return movieMapper.movieToDto(movie, url.toString());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(movies);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateMovie(@PathVariable String id, @ModelAttribute MovieDto dto) {
        try {
            Optional<EasyMovie> updatedMovie = movieService.updateMovie(id, dto);

            if (updatedMovie.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DuplicatedTupleException e) {
            Map<String, String> jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id){
        boolean deleted = movieService.delete(id);
        if (deleted){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    private URI buildMovieURL(EasyMovie easyMovie){
        String moviePath = "/" + easyMovie.getId();
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(moviePath)
                .build()
                .toUri();
    }

}
