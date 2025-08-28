package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.usecase.registry_user.command_queue.UsuarioValidationQueue;
import com.peryloth.usecase.registry_user.command_queue.validations.UsuarioValidation;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UsuarioValidationQueueTest {

    @Test
    void validate_sinValidaciones_completaOK() {
        UsuarioValidationQueue queue = new UsuarioValidationQueue();
        Usuario usuario = new Usuario();

        StepVerifier.create(queue.validate(usuario))
                .verifyComplete();
    }

    @Test
    void validate_conTodasValidacionesExitosas_completaOK() {
        // Mock de dos validaciones que siempre pasan
        UsuarioValidation v1 = mock(UsuarioValidation.class);
        UsuarioValidation v2 = mock(UsuarioValidation.class);

        when(v1.validate(any())).thenReturn(Mono.empty());
        when(v2.validate(any())).thenReturn(Mono.empty());

        UsuarioValidationQueue queue = new UsuarioValidationQueue()
                .addValidation(v1)
                .addValidation(v2);

        StepVerifier.create(queue.validate(new Usuario()))
                .verifyComplete();

        // Verifica que ambas validaciones fueron llamadas
        verify(v1).validate(any());
        verify(v2).validate(any());
    }

    @Test
    void validate_conUnaValidacionFalla_propagadoError() {
        // Mock de una validación que falla y otra que sería posterior
        UsuarioValidation v1 = mock(UsuarioValidation.class);
        UsuarioValidation v2 = mock(UsuarioValidation.class);

        when(v1.validate(any())).thenReturn(Mono.error(new IllegalArgumentException("Email inválido")));
        when(v2.validate(any())).thenReturn(Mono.empty());

        UsuarioValidationQueue queue = new UsuarioValidationQueue()
                .addValidation(v1)
                .addValidation(v2);

        StepVerifier.create(queue.validate(new Usuario()))
                .expectErrorMatches(t -> t instanceof IllegalArgumentException &&
                        t.getMessage().equals("Email inválido"))
                .verify();

        verify(v1).validate(any());
    }
}
