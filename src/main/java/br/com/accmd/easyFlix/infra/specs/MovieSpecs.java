package br.com.accmd.easyFlix.infra.specs;

import br.com.accmd.easyFlix.domain.entities.EasyMovie;
import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class MovieSpecs {

    public static Specification<EasyMovie> genreEquals(MovieGenre movieGenre){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("movieGenre"), movieGenre));
    }

    public static Specification<EasyMovie> titleLike(String movieTitle){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("movieTitle")), "%" + movieTitle.toUpperCase() + "%"));
    }
}
