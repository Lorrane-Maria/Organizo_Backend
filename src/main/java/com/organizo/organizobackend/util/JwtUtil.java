// src/main/java/com/organizo/organizobackend/util/JwtUtil.java
package com.organizo.organizobackend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Gera e valida tokens JWT usando HS256 com chave forte (≥256 bits).
 */
@Component
public class JwtUtil {

    // Gera uma chave forte para HS256 (256 bits) no startup:
    private final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // Validade em milissegundos (1 hora)
    private final long expirationMs = 3600_000;

    /** Gera um token com subject=email. */
    public String gerarToken(String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Extrai o subject (email) do token. */
    public String obterEmail(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
