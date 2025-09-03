package com.peryloth.jwtvalidation;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtTokenProvider {

    private static final Key KEY = Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    public static Mono<Boolean> validateTokenReactive(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parser() // ✅ en 0.13.0
                        .setSigningKey(KEY)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                log.error("❌ Token inválido: {}", e.getMessage());
                return false;
            }
        });
    }

}
