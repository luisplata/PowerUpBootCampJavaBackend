package com.peryloth.usecase.registry_user;

import com.peryloth.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface IRegistryUserUseCase {

    Mono<Void> registryUser(Usuario usuario);
}
