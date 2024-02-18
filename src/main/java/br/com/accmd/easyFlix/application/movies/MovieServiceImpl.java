package br.com.accmd.easyFlix.application.movies;

import br.com.accmd.easyFlix.infra.repository.EasyMovieRepo;
import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyMovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements EasyMovieService {

    private final EasyMovieRepo easyMovieRepo;

    @Override
    public EasyMovie getByMovieTitle(String movieTitle) {
        return easyMovieRepo.findByMovieTitle(movieTitle);
    }

    @Override
    @Transactional
    public EasyMovie save(EasyMovie easyMovie) {
        var possibleMovie = getByMovieTitle(easyMovie.getMovieTitle());

        if (possibleMovie != null){
            throw new DuplicatedTupleException("Filme já cadastrado");
        }

        return easyMovieRepo.save(easyMovie);

    }

    @Override
    public Optional<EasyMovie> getById(String id) {
        return easyMovieRepo.findById(id);
    }


    @Override
    public List<EasyMovie> search(MovieGenre genre, String query) {
        return easyMovieRepo.findByMovieTitleOrGenre(genre, query);
    }

    @Override
    @Transactional
    public Optional<EasyMovie> updateMovie(String id, MovieDto movieDto) throws DuplicatedTupleException, IOException {
        Optional<EasyMovie> existingMovie = getById(id);

        if (existingMovie.isPresent()) {
            EasyMovie movieToUpdate = existingMovie.get();

            // Atualize apenas os campos não nulos
            if (movieDto.getMovieTitle() != null) {
                movieToUpdate.setMovieTitle(movieDto.getMovieTitle());
            }
            // ... outros campos ...

            // Atualize o arquivo apenas se for fornecido
            if (movieDto.getFile() != null) {
                movieToUpdate.setFile(movieDto.getFile().getBytes());
            }

            return Optional.of(easyMovieRepo.save(movieToUpdate));
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(String id) {
        if (easyMovieRepo.existsById(id)){
            easyMovieRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
