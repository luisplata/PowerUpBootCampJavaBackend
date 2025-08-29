package com.peryloth.usecase.validationclient;

import reactor.core.publisher.Mono;

public interface IValidationClientUseCase {
    Mono<Boolean> IsUserValid(String jwt, String document, String email);
}
