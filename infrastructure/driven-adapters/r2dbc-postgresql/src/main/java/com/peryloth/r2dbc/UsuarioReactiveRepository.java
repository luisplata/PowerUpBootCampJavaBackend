package com.peryloth.r2dbc;

import com.peryloth.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.math.BigInteger;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, BigInteger>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

}
