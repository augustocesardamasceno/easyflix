package br.com.accmd.easyFlix.application.movies;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
public class MovieDto {
    private String id;
    private String url;
    private String movieTitle;
    private String movieGenre;
    private String duration;
    private String movieLink;
    private String country;
    private MultipartFile file;

}
