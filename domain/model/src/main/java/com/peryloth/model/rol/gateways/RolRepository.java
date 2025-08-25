package com.peryloth.model.rol.gateways;

import com.peryloth.model.rol.Rol;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface RolRepository {
    Mono<Rol> getRolById(BigInteger id);
}
