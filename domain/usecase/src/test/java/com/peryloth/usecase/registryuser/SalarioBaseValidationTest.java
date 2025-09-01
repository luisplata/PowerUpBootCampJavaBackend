package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.usecase.registry_user.command_queue.validations.SalarioBaseValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class SalarioBaseValidationTest {

    private SalarioBaseValidation validation;

    @BeforeEach
    void setUp() {
        validation = new SalarioBaseValidation();
    }

    @Test
    void shouldPassWhenSalarioIsValid() {
        Usuario usuario = Usuario.builder().salarioBase(5000L).build();

        StepVerifier.create(validation.validate(usuario))
                .verifyComplete(); // ✅ Mono vacío = validación pasó
    }

    @Test
    void shouldFailWhenSalarioIsNull() {
        Usuario usuario = Usuario.builder().salarioBase(null).build();

        StepVerifier.create(validation.validate(usuario))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().contains("no puede ser nulo o vacío"))
                .verify();
    }

    @Test
    void shouldFailWhenSalarioIsZeroOrNegative() {
        Usuario usuario = Usuario.builder().salarioBase(0L).build();

        StepVerifier.create(validation.validate(usuario))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().contains("no puede ser nulo o vacío"))
                .verify();

        Usuario usuarioNegativo = Usuario.builder().salarioBase(-10L).build();

        StepVerifier.create(validation.validate(usuarioNegativo))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().contains("no puede ser nulo o vacío"))
                .verify();
    }

    @Test
    void shouldFailWhenSalarioExceedsMaxValue() {
        Usuario usuario = Usuario.builder().salarioBase(20000000L).build();

        StepVerifier.create(validation.validate(usuario))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().contains("entre 0 y 15000000"))
                .verify();
    }
}
