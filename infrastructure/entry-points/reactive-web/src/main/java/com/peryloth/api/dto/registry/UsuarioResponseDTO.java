package com.peryloth.api.dto.registry;

public record UsuarioResponseDTO(
        String idUsuario,
        String nombre,
        String apellido,
        String email,
        String documentoIdentidad,
        String telefono,
        Long salarioBase,
        String rolNombre
) {}
