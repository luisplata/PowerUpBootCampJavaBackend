package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.usecase.registryuser.commandQueue.UsuarioValidationQueue;
import com.peryloth.usecase.registryuser.commandQueue.validations.ApellidoValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.EmailValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.NombreValidation;
import com.peryloth.usecase.registryuser.commandQueue.validations.SalarioBaseValidation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistryUserUseCase implements IRegistryUserUseCase {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Mono<Void> RegistryUser(Usuario usuario) throws IllegalArgumentException {
        UsuarioValidationQueue validationQueue = new UsuarioValidationQueue()
                .addValidation(new NombreValidation())
                .addValidation(new ApellidoValidation())
                .addValidation(new EmailValidation(usuarioRepository))
                .addValidation(new SalarioBaseValidation());

        return validationQueue.validate(usuario).then(usuarioRepository.saveUsuario(usuario));
    }
}
