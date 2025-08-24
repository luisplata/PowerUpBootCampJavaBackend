package com.peryloth.usecase.registryuser.commandQueue.validations;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public class ApellidoValidation implements UsuarioValidation {
    @Override
    public Mono<Void> validate(Usuario usuario) {
        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo apellido no puede ser nulo o vacío"));
        }
        return Mono.empty();
    }
}