package com.peryloth.usecase.registryuser.commandQueue.validations;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioValidation {
    Mono<Void> validate(Usuario usuario);
}

