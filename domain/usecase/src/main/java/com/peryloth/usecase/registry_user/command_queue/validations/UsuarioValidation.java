package com.peryloth.usecase.registry_user.command_queue.validations;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioValidation {
    Mono<Void> validate(Usuario usuario);
}

