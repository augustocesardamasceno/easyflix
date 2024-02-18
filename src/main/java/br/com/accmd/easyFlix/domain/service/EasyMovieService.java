package br.com.accmd.easyFlix.domain.service;

import br.com.accmd.easyFlix.application.movies.MovieDto;
import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EasyMovieService {
    @Transactional
    EasyMovie save (EasyMovie easyMovie);

    Optional<EasyMovie> getById(String id);
    EasyMovie getByMovieTitle(String movieTitle);

    List<EasyMovie> search(MovieGenre genre, String query);

    @Transactional
    Optional<EasyMovie> updateMovie(String id, MovieDto movieDto) throws DuplicatedTupleException, IOException;

    boolean delete(String id);

}
