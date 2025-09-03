package com.peryloth.usecase.validationclient;

import com.peryloth.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidationClientUseCase implements IValidationClientUseCase {
    private final UsuarioRepository usuarioRepository;
    private final IValidateJwt validateJwt;

    @Override
    public Mono<Boolean> isUserValid(String jwt, String document, String email) {
        return validateJwt.validate(jwt)
                .flatMap(isValid -> {
                    if (Boolean.TRUE.equals(isValid)) {
                        return usuarioRepository.getUsuarioByEmailAndDocument(email, document)
                                .flatMap(usuario ->
                                        Mono.just(usuario.getDocumentoIdentidad().equalsIgnoreCase(document))
                                ).defaultIfEmpty(false);
                    } else {
                        return Mono.error(new IllegalArgumentException("JWT no es válido"));
                    }
                });
    }
}
