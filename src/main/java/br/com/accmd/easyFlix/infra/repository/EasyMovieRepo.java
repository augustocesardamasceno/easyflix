package br.com.accmd.easyFlix.infra.repository;

import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import br.com.accmd.easyFlix.infra.specs.GenericSpecs;
import br.com.accmd.easyFlix.infra.specs.MovieSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static br.com.accmd.easyFlix.infra.specs.GenericSpecs.conjunction;
import static br.com.accmd.easyFlix.infra.specs.MovieSpecs.genreEquals;
import static org.springframework.data.jpa.domain.Specification.where;

public interface EasyMovieRepo extends JpaRepository<EasyMovie, String>, JpaSpecificationExecutor<EasyMovie> {

    EasyMovie findByMovieTitle(String movieTitle);
    Optional<EasyMovie> findById(String id);

    default List<EasyMovie> findByMovieTitleOrGenre(MovieGenre movieGenre, String query) {
        Specification<EasyMovie> specification = where(conjunction());

        if (movieGenre != null) {
            specification = specification.and(genreEquals(movieGenre));
        }

        if (StringUtils.hasText(query)) {
            specification = specification.and(MovieSpecs.titleLike(query));

        }

        return findAll(specification);
    }



}
