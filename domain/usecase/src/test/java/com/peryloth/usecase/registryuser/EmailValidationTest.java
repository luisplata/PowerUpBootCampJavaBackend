package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.usecase.registry_user.command_queue.validations.EmailValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmailValidationTest {

    private UsuarioRepository usuarioRepository;
    private EmailValidation emailValidation;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        emailValidation = new EmailValidation(usuarioRepository);
    }

    @Test
    void validate_emailNull_error() {
        Usuario usuario = Usuario.builder().email(null).build();

        StepVerifier.create(emailValidation.validate(usuario))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("no puede ser nulo"))
                .verify();
    }

    @Test
    void validate_emailVacio_error() {
        Usuario usuario = Usuario.builder().email("").build();

        StepVerifier.create(emailValidation.validate(usuario))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("no puede ser nulo"))
                .verify();
    }

    @Test
    void validate_emailFormatoInvalido_error() {
        Usuario usuario = Usuario.builder().email("correo_invalido").build();

        StepVerifier.create(emailValidation.validate(usuario))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("formato de email válido"))
                .verify();
    }

    @Test
    void validate_emailNoExiste_completaOK() {
        Usuario usuario = Usuario.builder().email("test@example.com").build();

        when(usuarioRepository.getUsuarioByEmail(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(emailValidation.validate(usuario))
                .verifyComplete();

        verify(usuarioRepository).getUsuarioByEmail("test@example.com");
    }

    @Test
    void validate_emailYaExiste_error() {
        Usuario usuario = Usuario.builder().email("test@example.com").build();

        when(usuarioRepository.getUsuarioByEmail("test@example.com"))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(emailValidation.validate(usuario))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("Correo ya existe"))
                .verify();

        verify(usuarioRepository).getUsuarioByEmail("test@example.com");
    }
}
