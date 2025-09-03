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

    private static final String SECRET_KEY = "claveSuperSecretaDeAlMenos32Caracteres!!";
    private static final long EXPIRATION_TIME = 3600_000;

    private static final Logger log = LoggerFactory.getLogger(JwtValidacionAdapter.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Boolean> validate(String jwt) {
        return Mono.justOrEmpty(jwt)
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .doOnNext(t -> log.debug("Token extraído: {}", t))
                .map(JwtTokenProvider::validateTokenReactive)
                .doOnNext(valid -> {
                    log.error("JWT inválido para el token extraído");
                })
                .defaultIfEmpty(Mono.just(false))
                .doOnError(e -> log.error("Error al validar el JWT: {}", e.getMessage()))
                .flatMap(mono -> mono);
    }

    public Mono<String> createToken(String email) {
        return Mono.fromSupplier(() ->
                Jwts.builder()
                        .setSubject(email)
                        .setIssuer("hu2-service")
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact()
        );
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
