package br.com.accmd.easyFlix.application.movies;

import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MovieMapper {

    public EasyMovie mapToEasyMovie(MultipartFile file, String movieTitle, String duration,
                                    String country, MovieGenre movieGenre, String movieLink) throws IOException {
        return EasyMovie.builder()
                .movieTitle(movieTitle)
                .duration(duration)
                .country(country)
                .movieGenre(movieGenre)
                .movieLink(movieLink)
                .file(file.getBytes())
                .build();
    }

    public MovieDto movieToDto(EasyMovie easyMovie, String url) {
        return MovieDto.builder()
                .id(easyMovie.getId())
                .url(url)
                .movieGenre(easyMovie.getMovieGenre().name())
                .movieTitle(easyMovie.getMovieTitle())
                .movieLink(easyMovie.getMovieLink())
                .duration(easyMovie.getDuration())
                .country(easyMovie.getCountry())
                .build();

    }

}
