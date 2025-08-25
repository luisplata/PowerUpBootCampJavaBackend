package com.peryloth.usecase.registry_user.command_queue.validations;


import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public class NombreValidation implements UsuarioValidation {
    @Override
    public Mono<Void> validate(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo nombre no puede ser nulo o vacío"));
        }
        return Mono.empty();
    }
}