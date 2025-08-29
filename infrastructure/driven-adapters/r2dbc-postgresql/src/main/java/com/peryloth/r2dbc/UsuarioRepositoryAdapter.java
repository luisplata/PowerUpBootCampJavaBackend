package com.peryloth.r2dbc;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.r2dbc.entity.UsuarioEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
        BigInteger,
        UsuarioReactiveRepository
        > implements UsuarioRepository {

    private static final Logger log = LoggerFactory.getLogger(UsuarioRepositoryAdapter.class);

    public UsuarioRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    @Transactional
    public Mono<Usuario> saveUsuario(Usuario usuario) {
        UsuarioEntity entity = mapper.map(usuario, UsuarioEntity.class);
        BigInteger rolId = usuario != null && usuario.getRol() != null ? usuario.getRol().getUniqueId() : null;
        entity.setRolId(rolId);
        return repository.save(entity)
                .map(saved -> {
                    Usuario mapped = mapper.map(saved, Usuario.class);
                    if (saved.getRolId() != null) {
                        mapped.setRol(Rol.builder().uniqueId(saved.getRolId()).build());
                    }
                    return mapped;
                })
                .doOnNext(u -> log.info("Usuario guardado con id={}", u.getIdUsuario()));
    }

    @Override
    public Mono<Usuario> getUsuarioByEmail(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        return findByExample(usuario)
                .next()
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Usuario> getUsuarioByEmailAndDocument(String email, String document) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setDocumentoIdentidad(document);
        return findByExample(usuario)
                .next()
                .switchIfEmpty(Mono.empty());
    }
}
