package com.peryloth.jwtvalidation;

import com.peryloth.usecase.validationclient.IValidateJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtValidacionAdapter implements IValidateJwt {

    @Override
    public Mono<Boolean> validate(String jwt) {

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            return Mono.just(false);
        }

        String token = jwt.substring(7);

        System.out.println("Token extraído: " + token);

        // 2. Validar JWT
        return !JwtTokenProvider.validateToken(token) ? Mono.just(false) : Mono.just(true);

    }
}
