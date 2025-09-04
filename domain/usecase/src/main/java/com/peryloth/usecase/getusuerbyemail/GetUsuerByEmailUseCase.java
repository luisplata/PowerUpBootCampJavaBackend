package com.peryloth.usecase.getusuerbyemail;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetUsuerByEmailUseCase implements IGetUsuerByEmailUseCase {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Mono<Usuario> getUserByEmailAndDocument(String email) {
        return usuarioRepository.getUsuarioByEmail(email);
    }
}
