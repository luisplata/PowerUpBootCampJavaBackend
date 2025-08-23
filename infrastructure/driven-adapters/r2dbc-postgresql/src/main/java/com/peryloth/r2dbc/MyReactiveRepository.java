package com.peryloth.r2dbc;

import com.peryloth.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.math.BigInteger;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, BigInteger>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

}
