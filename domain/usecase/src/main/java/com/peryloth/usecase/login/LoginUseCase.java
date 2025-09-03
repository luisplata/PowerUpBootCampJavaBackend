package com.peryloth.usecase.login;

import com.peryloth.model.usuario.gateways.PasswordEncoder;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase implements ILogin {
    private final UsuarioRepository usuarioRepository;
    private final IJwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> login(String email, String password) {
        return usuarioRepository.getUsuarioByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no encontrado")))
                .flatMap(usuario -> {
                    if (passwordEncoder.matches(password, usuario.getPasswordHash())) {
                        return Mono.just(jwtTokenProvider.createToken(usuario.getEmail()));
                    }
                    return Mono.error(new IllegalArgumentException("Credenciales inválidas"));
                }).flatMap(token -> token);
    }
}
