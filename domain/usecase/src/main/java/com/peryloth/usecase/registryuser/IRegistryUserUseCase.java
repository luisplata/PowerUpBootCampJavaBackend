package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface IRegistryUserUseCase {

    public Mono<Void> RegistryUser(Usuario usuario);
}
