package com.peryloth.usecase.registryuser;

import com.peryloth.model.rol.gateways.RolRepository;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.usecase.registryuser.commandQueue.UsuarioValidationQueue;
import com.peryloth.usecase.registryuser.commandQueue.validations.ApellidoValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.EmailValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.NombreValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.SalarioBaseValidation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


@RequiredArgsConstructor
public class RegistryUserUseCase implements IRegistryUserUseCase {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public Mono<Void> RegistryUser(Usuario usuario) {
        UsuarioValidationQueue validationQueue = new UsuarioValidationQueue()
                .addValidation(new NombreValidation())
                .addValidation(new ApellidoValidation())
                .addValidation(new EmailValidation(usuarioRepository))
                .addValidation(new SalarioBaseValidation());

        BigInteger rolIdFijo = BigInteger.ONE;

        return validationQueue.validate(usuario)
                .then(rolRepository.getRolById(rolIdFijo)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Rol fijo no encontrado")))
                        .flatMap(rol -> {
                            Usuario usuarioConRol = usuario.toBuilder().rol(rol).build();
                            System.out.println("Usuario con rol asignado: " + usuarioConRol.getRol().getUniqueId());
                            return usuarioRepository.saveUsuario(usuarioConRol);
                        })
                ).then();
    }
}
