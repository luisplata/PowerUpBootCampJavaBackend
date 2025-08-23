package com.peryloth.usecase.registryuser;

import com.peryloth.model.usuario.Usuario;
import com.peryloth.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class RegistryUserUseCase implements IRegistryUserUseCase {
    private final UsuarioRepository usuarioRepository;
    @Override
    public void RegistryUser(Usuario usuario) {
        usuarioRepository.saveUsuario(usuario);
    }
}
