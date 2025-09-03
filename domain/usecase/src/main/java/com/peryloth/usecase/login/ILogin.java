package com.peryloth.usecase.login;

import reactor.core.publisher.Mono;

public interface ILogin {

    Mono<String> login(String email, String password);
}
