package com.peryloth.usecase.getusuerbyemail;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.model.rol.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class GetUsuerByEmailUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private GetUsuerByEmailUseCase getUsuerByEmailUseCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        getUsuerByEmailUseCase = new GetUsuerByEmailUseCase(usuarioRepository);
    }

    @Test
    void shouldReturnUsuarioWhenFound() {
        // Arrange
        String email = "test@example.com";
        Usuario usuario = Usuario.builder()
                .idUsuario(BigInteger.ONE)
                .nombre("Luis")
                .apellido("Plata")
                .email(email)
                .documentoIdentidad("123456789")
                .telefono("3001234567")
                .rol(new Rol(BigInteger.ONE, "ADMIN", "ADMIN"))
                .salarioBase(5000L)
                .passwordHash("hash123")
                .intentosFallidos(0)
                .bloqueadoHasta(null)
                .build();

        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.just(usuario));

        // Act & Assert
        StepVerifier.create(getUsuerByEmailUseCase.getUserByEmailAndDocument(email))
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(getUsuerByEmailUseCase.getUserByEmailAndDocument(email))
                .verifyComplete();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        // Arrange
        String email = "error@example.com";
        RuntimeException ex = new RuntimeException("DB error");
        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.error(ex));

        // Act & Assert
        StepVerifier.create(getUsuerByEmailUseCase.getUserByEmailAndDocument(email))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("DB error"))
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
    }
}
