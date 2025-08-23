package com.peryloth.model.usuario.gateways;

import com.peryloth.model.usuario.Usuario;

public interface UsuarioRepository {
    void saveUsuario(Usuario usuario);
}
