package com.peryloth.jwtvalidation;

import com.peryloth.usecase.validationclient.IValidateJwt;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtValidacionAdapter implements IValidateJwt {

    private static final Logger log = LoggerFactory.getLogger(JwtValidacionAdapter.class);

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

}
