package com.peryloth.api.DTO.registry;

public record UsuarioRequestDTO(
        String nombre,
        String apellido,
        String email,
        String documentoIdentidad,
        String telefono,
        Long salarioBase
) {
}
