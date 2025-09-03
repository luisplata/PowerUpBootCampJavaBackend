package com.peryloth.usecase.login;

import reactor.core.publisher.Mono;

public interface IJwtTokenProvider {
    Mono<String> createToken(String email);
}
