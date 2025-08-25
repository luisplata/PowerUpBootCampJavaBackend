package com.peryloth.r2dbc;

import com.peryloth.model.rol.Rol;
import com.peryloth.model.rol.gateways.RolRepository;
import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.r2dbc.entity.UsuarioEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
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

    public UsuarioRepositoryAdapter(UsuarioReactiveRepository repository, RolReactiveRepository rolRepository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public Mono<Usuario> saveUsuario(Usuario usuario) {
        return save(usuario) // suponiendo que esto retorna Mono<Usuario>
                .doOnNext(u -> log.info("Usuario guardado con id={}", u.getIdUsuario()));
    }

    @Override
    public Mono<Usuario> getUsuarioByEmail(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        return findByExample(usuario).next();
    }
}
