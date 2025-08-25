package com.peryloth.usecase.registry_user;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.rol.gateways.RolRepository;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistryUserUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private RegistryUserUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);
        useCase = new RegistryUserUseCase(usuarioRepository, rolRepository);
    }

    @Test
    void registryUser_conUsuarioValidoYRolExistente_guardaUsuario() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .idUsuario(BigInteger.TEN)
                .nombre("Carlos")
                .apellido("Pérez")
                .email("carlos@example.com")
                .salarioBase(3000L)
                .build();

        Rol rol = new Rol(BigInteger.ONE, "ADMIN", "Administrador");

        when(usuarioRepository.getUsuarioByEmail(usuario.getEmail()))
                .thenReturn(Mono.empty());
        when(rolRepository.getRolById(any()))
                .thenReturn(Mono.just(rol));
        when(usuarioRepository.saveUsuario(any()))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    // Asegurarte que el usuario tenga el rol asignado
                    u.setRol(rol);
                    return Mono.just(u);
                });

        // Act & Assert
        StepVerifier.create(useCase.registryUser(usuario))
                .expectNextMatches(u -> u.getNombre().equals("Carlos") &&
                        u.getRol() != null &&
                        u.getRol().getNombre().equals("ADMIN"))
                .verifyComplete();

        // Verificar que el usuario se guardó
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).saveUsuario(captor.capture());
        Usuario usuarioGuardado = captor.getValue();
        assertEquals("Carlos", usuarioGuardado.getNombre());
        assertEquals("ADMIN", usuarioGuardado.getRol().getNombre());
    }


    @Test
    void registryUser_conRolNoExistente_error() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .idUsuario(BigInteger.TEN)
                .nombre("Ana")
                .apellido("López")
                .email("ana@example.com")
                .salarioBase(2500L)
                .build();

        when(usuarioRepository.getUsuarioByEmail(usuario.getEmail()))
                .thenReturn(Mono.empty());
        when(rolRepository.getRolById(any()))
                .thenReturn(Mono.empty()); // simulamos rol inexistente

        // Act & Assert
        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Rol fijo no encontrado"))
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }

    @Test
    void registryUser_conUsuarioInvalido_porValidacionFalla() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .idUsuario(BigInteger.TEN)
                .nombre("Ana")
                .apellido("López")
                .email("correo-invalido")
                .salarioBase(2500L)
                .build();

        // Mock obligatorio para evitar NullPointerException
        when(usuarioRepository.getUsuarioByEmail(anyString()))
                .thenReturn(Mono.empty());
        when(rolRepository.getRolById(any()))
                .thenReturn(Mono.just(new Rol(BigInteger.ONE, "ADMIN", "Administrador")));

        // Act & Assert
        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException)
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }
}
