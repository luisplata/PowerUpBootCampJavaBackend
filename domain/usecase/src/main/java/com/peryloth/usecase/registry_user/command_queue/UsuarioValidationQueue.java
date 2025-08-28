package com.peryloth.usecase.registry_user.command_queue;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.usecase.registry_user.command_queue.validations.UsuarioValidation;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class UsuarioValidationQueue {
    private final List<UsuarioValidation> validations = new ArrayList<>();

    public UsuarioValidationQueue addValidation(UsuarioValidation validation) {
        validations.add(validation);
        return this;
    }

    public Mono<Void> validate(Usuario usuario) {
        Mono<Void> result = Mono.empty();
        for (UsuarioValidation validation : validations) {
            result = result.then(validation.validate(usuario));
        }
        return result;
    }
}
