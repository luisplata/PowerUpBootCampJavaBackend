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
    private final ObjectMapper mapper;

    public RolRepositoryAdapter(RolReactiveRepository rolRepository, ObjectMapper mapper) {
        super(rolRepository, mapper, d -> mapper.map(d, Rol.class));
        this.rolRepository = rolRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Rol> getRolById(BigInteger id) {
        return rolRepository.findById(id)
                .map(rolEntity -> mapper.map(rolEntity, Rol.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Rol no encontrado con id=" + id)));
    }
}
