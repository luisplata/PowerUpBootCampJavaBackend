package com.peryloth.r2dbc;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.r2dbc.entity.UsuarioEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
        BigInteger,
        MyReactiveRepository
        > implements UsuarioRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public Mono<Void> saveUsuario(Usuario usuario) {
        System.out.println("Guardando usuario: " + usuario.getNombre());
        return save(usuario).then();
    }

    @Override
    public Mono<Usuario> getUsuarioByEmail(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        return findByExample(usuario).next();
    }
}
