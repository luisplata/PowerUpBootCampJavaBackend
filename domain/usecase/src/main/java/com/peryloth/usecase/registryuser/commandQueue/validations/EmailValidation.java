package com.peryloth.usecase.registryuser.commandQueue.validations;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;

public class EmailValidation implements UsuarioValidation {
    private final UsuarioRepository usuarioRepository;

    public EmailValidation(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Mono<Void> validate(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El campo correo_electronico no puede ser nulo o vacío"));
        }
        // el correo_electronico tenga un formato de email válido.
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!usuario.getEmail().matches(emailRegex)) {
            return Mono.error(new IllegalArgumentException("El campo correo_electronico debe tener un formato de email válido"));
        }
        return usuarioRepository.getUsuarioByEmail(usuario.getEmail())
                .flatMap(existing -> Mono.error(new IllegalArgumentException("Correo ya existe")))
                .then();
    }
}