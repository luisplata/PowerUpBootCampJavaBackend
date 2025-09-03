package com.peryloth.jwtvalidation;

import com.peryloth.model.usuario.gateways.PasswordEncoder;
import com.peryloth.usecase.login.IJwtTokenProvider;
import com.peryloth.usecase.validationclient.IValidateJwt;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@RequiredArgsConstructor
public class JwtValidacionAdapter implements IValidateJwt, IJwtTokenProvider, PasswordEncoder {

    private static final long EXPIRATION_TIME = 3600_000;

    private static final Logger log = LoggerFactory.getLogger(JwtValidacionAdapter.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Key KEY_BY_MICRO = Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    public Mono<Boolean> validate(String jwt) {
        return Mono.justOrEmpty(jwt)
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .flatMap(JwtTokenProvider::validateTokenReactive) // 👈 directo flatMap
                .onErrorResume(e -> {
                    log.error("Error al validar JWT: {}", e.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<String> createToken(String email) {
        return Mono.fromSupplier(() ->
                Jwts.builder()
                        .setSubject(email)
                        .setIssuer("hu2-service")
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(KEY_BY_MICRO, SignatureAlgorithm.HS256)
                        .compact()
        );
    }

    @Override
    public Mono<String> getUsernameFromToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = Jwts.parser() // ✅ en 0.13.0
                        .setSigningKey(KEY_BY_MICRO)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.getSubject();
            } catch (JwtException | IllegalArgumentException e) {
                log.error("❌ Error al extraer el username del token: {}", e.getMessage());
                return null;
            }
        });
    }


    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
