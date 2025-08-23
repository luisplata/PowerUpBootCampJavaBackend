package com.peryloth.r2dbc;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import com.peryloth.r2dbc.entity.UsuarioEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario/* change for domain model */,
        UsuarioEntity/* change for adapter model */,
        Long,
        MyReactiveRepository
        > implements UsuarioRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public void saveUsuario(Usuario usuario) {
        save(usuario).subscribe();
    }
}
