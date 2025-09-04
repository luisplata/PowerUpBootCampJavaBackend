package com.peryloth.usecase.login;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.PasswordEncoder;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private IJwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        jwtTokenProvider = Mockito.mock(IJwtTokenProvider.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        loginUseCase = new LoginUseCase(usuarioRepository, jwtTokenProvider, passwordEncoder);
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String passwordHash = "hash123";
        String expectedToken = "jwt-token";

        Usuario usuario = Usuario.builder()
                .idUsuario(BigInteger.ONE)
                .nombre("Luis")
                .apellido("Plata")
                .email(email)
                .documentoIdentidad("123456789")
                .telefono("3001234567")
                .rol(new Rol(BigInteger.ONE, "ADMIN", "ADMIN"))
                .salarioBase(5000L)
                .passwordHash(passwordHash)
                .build();

        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches(password, passwordHash)).thenReturn(true);
        when(jwtTokenProvider.createToken(email)).thenReturn(Mono.just(expectedToken));

        // Act & Assert
        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(expectedToken)
                .verifyComplete();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, passwordHash);
        verify(jwtTokenProvider, times(1)).createToken(email);
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        String password = "any";

        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(loginUseCase.login(email, password))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Usuario no encontrado"))
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }

    @Test
    void shouldReturnErrorWhenPasswordDoesNotMatch() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        String passwordHash = "hash123";

        Usuario usuario = Usuario.builder()
                .email(email)
                .passwordHash(passwordHash)
                .build();

        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.just(usuario));
        when(passwordEncoder.matches(password, passwordHash)).thenReturn(false);

        // Act & Assert
        StepVerifier.create(loginUseCase.login(email, password))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Credenciales inválidas"))
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, passwordHash);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        // Arrange
        String email = "error@example.com";
        String password = "any";
        RuntimeException exception = new RuntimeException("DB error");

        when(usuarioRepository.getUsuarioByEmail(email)).thenReturn(Mono.error(exception));

        // Act & Assert
        StepVerifier.create(loginUseCase.login(email, password))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("DB error"))
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }
}
