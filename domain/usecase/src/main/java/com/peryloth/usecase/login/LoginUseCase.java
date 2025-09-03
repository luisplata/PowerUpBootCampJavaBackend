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
        System.out.println("Login - password antes de buscar usuario: " + password);
        return usuarioRepository.getUsuarioByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no encontrado")))
                .flatMap(usuario -> {
                    System.out.println("Login - email: " + usuario.getEmail());
                    System.out.println("Login - password enviado: " + password);
                    System.out.println("Login - password hash en DB: " + usuario.getPasswordHash());
                    if (passwordEncoder.matches(password, usuario.getPasswordHash())) {
                        return jwtTokenProvider.createToken(usuario.getEmail());
                    }
                    return Mono.error(new IllegalArgumentException("Credenciales inválidas"));
                });
    }
}
