package br.com.accmd.easyFlix.domain.enums;

import lombok.Getter;

import java.util.Arrays;

public enum MovieGenre {
    ACAO("Ação"),
    AVENTURA("Aventura"),
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    ROMANCE("Romance"),
    TERROR("Terror"),
    ANIMACAO("Animação"),
    DOCUMENTARIO("Documentário");

    @Getter
    private String movieGenre;

    MovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }


    public static MovieGenre ofName(String movieTitle){
        return Arrays.stream(values())
                .filter(movieGenre -> movieGenre.name().equalsIgnoreCase(movieTitle))
                .findFirst()
                .orElse(null);
    }
}
