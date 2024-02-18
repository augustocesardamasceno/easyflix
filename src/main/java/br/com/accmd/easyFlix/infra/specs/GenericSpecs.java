package br.com.accmd.easyFlix.infra.specs;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class GenericSpecs {

    public static <T> Specification<T> conjunction(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

}
