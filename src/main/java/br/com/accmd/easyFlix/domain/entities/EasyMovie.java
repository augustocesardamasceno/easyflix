package br.com.accmd.easyFlix.domain.entities;

import br.com.accmd.easyFlix.domain.enums.MovieGenre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "easy_movie")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EasyMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column
    private String movieTitle;
    @Column
    private String duration;
    @Column
    private String country;
    @Column
    @Enumerated(EnumType.STRING)
    private MovieGenre movieGenre;
    @Column
    private String movieLink;
    @Column
    @Lob //informa que esse campo corresponde a um arquivo
    private byte[] file;

    public String getFileName(){
        return getMovieTitle().concat(".").concat(getMovieGenre().name());
    }

}
