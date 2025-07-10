package br.com.tech.challenge.services;

import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {

    @Value("${jwt.secret.key}")
    private String jwtKey;

    @Value("${jwt.expiration.time:3600}")
    private Long jwtExpiration;

    public TokenService(@Value("${jwt.secret.key}") String jwtKey,
                        @Value("${jwt.expiration.time:3600}") Long jwtExpiration) {
        this.jwtKey = jwtKey;
        this.jwtExpiration = jwtExpiration;
    }

    public String gerarToken(Usuario usuario, Perfil perfil) {
        return Jwts.builder()
                .subject(usuario.getLogin())
                .claim("authorities", List.of(perfil.getTipo()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.SECONDS)))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)), Jwts.SIG.HS256)
                .compact();
    }

    public String extrairLogin(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Boolean validarToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)))
                .build()
                .parseSignedClaims(token);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private Boolean tokenExpirado(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(Date.from(Instant.now()));
    }

    public Boolean validarToken(String token, UserDetails userDetails) {
        final String login = extrairLogin(token);
        return (login.equals(userDetails.getUsername()) && !tokenExpirado(token));
    }
}
