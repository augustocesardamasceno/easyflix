package br.com.accmd.easyFlix.application.jwt.user;

import br.com.accmd.easyFlix.application.jwt.SecretKeyGenerator;
import br.com.accmd.easyFlix.domain.AccessToken;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceUser {

    private final SecretKeyGenerator keyGenerator;
    public AccessToken generateToken(EasyUser easyUser){

        SecretKey key = keyGenerator.getKey();
        Date expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(easyUser);

        String token = Jwts
                .builder()
                .signWith(key)
                .subject(easyUser.getEmail())
                .expiration(expirationDate)
                .claims(claims)
                .compact();

        return new AccessToken(token);
    }

    private Date generateExpirationDate(){
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(60);

        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(EasyUser easyUser){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", easyUser.getName());
        return claims;
    }

}
