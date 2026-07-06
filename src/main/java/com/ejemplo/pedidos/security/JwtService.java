package com.ejemplo.pedidos.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_MS = 1000 * 60 * 60;

    public String generarToken(String username){
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime()+ EXPIRATION_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getClave())
                .compact();
    }

    public String extraerUsername(String token){
        return Jwts.parser()
                .verifyWith(getClave())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean tokenEsValido(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getClave())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    private SecretKey getClave(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
