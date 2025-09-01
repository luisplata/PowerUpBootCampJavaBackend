package com.peryloth.usecase.validationclient;

import reactor.core.publisher.Mono;

public interface IValidateJwt {
    Mono<Boolean> validate(String jwt);
}
