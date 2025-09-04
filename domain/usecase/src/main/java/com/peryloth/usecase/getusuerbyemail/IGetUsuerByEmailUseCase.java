package com.peryloth.usecase.getusuerbyemail;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface IGetUsuerByEmailUseCase {
    Mono<Usuario> getUserByEmailAndDocument(String email);
}
