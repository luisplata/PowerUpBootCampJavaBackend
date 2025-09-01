package com.peryloth.model.usuario.gateways;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario> saveUsuario(Usuario usuario);
    Mono<Usuario> getUsuarioByEmail(String email);
    Mono<Usuario> getUsuarioByEmailAndDocument(String email, String document);
}
