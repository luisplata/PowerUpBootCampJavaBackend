package com.peryloth.r2dbc;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.rol.gateways.RolRepository;
import com.peryloth.r2dbc.entity.RolEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class RolRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        BigInteger,
        RolReactiveRepository
        > implements RolRepository {

    private static final Logger log = LoggerFactory.getLogger(RolRepositoryAdapter.class);

    private final RolReactiveRepository rolRepository;
    private final ObjectMapper mapperRol;

    public RolRepositoryAdapter(RolReactiveRepository rolRepository, ObjectMapper mapper) {
        super(rolRepository, mapper, d -> mapper.map(d, Rol.class));
        this.rolRepository = rolRepository;
        this.mapperRol = mapper;
    }

    @Override
    public Mono<Rol> getRolById(BigInteger id) {
        if (rolRepository == null) {
            return Mono.error(new IllegalStateException("RolReactiveRepository no inicializado"));
        }

        log.info("Buscando Rol con id={}", id);

        return rolRepository.findById(id)
                .doOnNext(rolEntity -> log.debug("Entidad encontrada: {}", rolEntity))
                .map(rolEntity -> mapperRol.map(rolEntity, Rol.class))
                .doOnNext(rol -> log.info("Rol mapeado correctamente: {}", rol))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No se encontró Rol con id={}", id);
                    return Mono.error(new IllegalArgumentException("Rol no encontrado con id=" + id));
                }))
                .doOnError(error -> log.error("Error al obtener Rol con id={}: {}", id, error.getMessage(), error));
    }
}
