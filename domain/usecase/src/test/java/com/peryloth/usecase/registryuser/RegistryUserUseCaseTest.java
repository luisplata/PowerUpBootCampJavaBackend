package com.peryloth.usecase.registryuser;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.rol.gateways.RolRepository;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.PasswordEncoder;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.usecase.registry_user.RegistryUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

class RegistryUserUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private RegistryUserUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        useCase = new RegistryUserUseCase(usuarioRepository, rolRepository, passwordEncoder);

        // Valores por defecto para evitar NullPointer
        when(usuarioRepository.getUsuarioByEmail(anyString())).thenReturn(Mono.empty());
        when(rolRepository.getRolById(any())).thenReturn(Mono.just(new Rol(BigInteger.ONE, "USER", "USER")));
        Usuario dummy = Usuario.builder()
                .idUsuario(BigInteger.ONE)
                .nombre("Test")
                .apellido("User")
                .email("test@correo.com")
                .salarioBase(1000L)
                .build();

        when(usuarioRepository.saveUsuario(any())).thenReturn(Mono.just(dummy));
    }

    private Usuario buildValidUser() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@test.com")
                .salarioBase(2000000L)
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Usuario usuario = buildValidUser();
        Rol rol = new Rol(BigInteger.ONE, "USER", "USER");

        when(rolRepository.getRolById(BigInteger.ONE)).thenReturn(Mono.just(rol));
        when(usuarioRepository.getUsuarioByEmail(usuario.getEmail())).thenReturn(Mono.empty());
        when(usuarioRepository.saveUsuario(any(Usuario.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.registryUser(usuario))
                .expectNextMatches(u -> u.getRol().equals(rol))
                .verifyComplete();

        verify(usuarioRepository).saveUsuario(any(Usuario.class));
    }

    @Test
    void shouldFailWhenNombreIsEmpty() {
        Usuario usuario = buildValidUser().toBuilder().nombre("").build();

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("nombre"))
                .verify();
    }

    @Test
    void shouldFailWhenApellidoIsNull() {
        Usuario usuario = buildValidUser().toBuilder().apellido(null).build();

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("apellido"))
                .verify();
    }

    @Test
    void shouldFailWhenEmailInvalid() {
        Usuario usuario = buildValidUser().toBuilder().email("invalidEmail").build();

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("formato de email"))
                .verify();
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        Usuario usuario = buildValidUser();

        when(usuarioRepository.getUsuarioByEmail(usuario.getEmail()))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("Correo ya existe"))
                .verify();
    }

    @Test
    void shouldFailWhenSalarioBaseIsInvalid() {
        Usuario usuario = buildValidUser().toBuilder().salarioBase(0L).build();

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().contains("salario_base"))
                .verify();
    }

    @Test
    void shouldFailWhenRolNotFound() {
        Usuario usuario = buildValidUser();

        when(usuarioRepository.getUsuarioByEmail(usuario.getEmail())).thenReturn(Mono.empty());
        when(rolRepository.getRolById(BigInteger.ONE)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.registryUser(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Rol fijo no encontrado"))
                .verify();
    }
}
