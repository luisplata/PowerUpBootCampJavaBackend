package com.peryloth.usecase.registryuser.commandQueue.validations;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public class SalarioBaseValidation implements UsuarioValidation {
    @Override
    public Mono<Void> validate(Usuario usuario) {
        if (usuario.getSalarioBase() == null || usuario.getSalarioBase() <= 0) {
            return Mono.error(new IllegalArgumentException("El campo salario_base no puede ser nulo o vacío"));
        }
        //salario_base sea un valor numérico (este entre 0 y 15000000)
        if (usuario.getSalarioBase() < 0 || usuario.getSalarioBase() > 15000000) {
            return Mono.error(new IllegalArgumentException("El campo salario_base debe ser un valor numérico entre 0 y 15000000"));
        }
        return Mono.empty();
    }
}