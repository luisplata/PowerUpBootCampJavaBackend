package com.peryloth.usecase.validationclient;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ValidationClientUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private IValidateJwt validateJwt;
    private ValidationClientUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        validateJwt = mock(IValidateJwt.class);
        useCase = new ValidationClientUseCase(usuarioRepository, validateJwt);
    }

    @Test
    void shouldReturnTrueWhenJwtValidAndUserFoundWithMatchingDocument() {
        // Arrange
        String jwt = "validJwt";
        String email = "test@email.com";
        String document = "123";

        Usuario usuario = new Usuario();
        usuario.setDocumentoIdentidad("123");

        when(validateJwt.validate(jwt)).thenReturn(Mono.just(true));
        when(usuarioRepository.getUsuarioByEmailAndDocument(email, document)).thenReturn(Mono.just(usuario));

        // Act & Assert
        StepVerifier.create(useCase.isUserValid(jwt, document, email))
                .expectNext(true)
                .verifyComplete();

        verify(validateJwt).validate(jwt);
        verify(usuarioRepository).getUsuarioByEmailAndDocument(email, document);
    }

    @Test
    void shouldReturnFalseWhenJwtValidAndUserFoundWithDifferentDocument() {
        String jwt = "validJwt";
        String email = "test@email.com";
        String document = "123";

        Usuario usuario = new Usuario();
        usuario.setDocumentoIdentidad("456");

        when(validateJwt.validate(jwt)).thenReturn(Mono.just(true));
        when(usuarioRepository.getUsuarioByEmailAndDocument(email, document)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.isUserValid(jwt, document, email))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenJwtValidAndUserNotFound() {
        String jwt = "validJwt";
        String email = "test@email.com";
        String document = "123";

        when(validateJwt.validate(jwt)).thenReturn(Mono.just(true));
        when(usuarioRepository.getUsuarioByEmailAndDocument(email, document)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.isUserValid(jwt, document, email))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenJwtInvalid() {
        String jwt = "invalidJwt";
        String email = "test@email.com";
        String document = "123";

        when(validateJwt.validate(jwt)).thenReturn(Mono.just(false));

        StepVerifier.create(useCase.isUserValid(jwt, document, email))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("JWT no es válido"))
                .verify();
    }
}
