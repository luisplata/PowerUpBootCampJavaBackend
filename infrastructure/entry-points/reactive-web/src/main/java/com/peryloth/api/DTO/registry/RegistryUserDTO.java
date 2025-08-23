package com.peryloth.api.DTO.registry;

import java.time.LocalDate;

public record RegistryUserDTO(Long id, String nombre, String apellido, String email, LocalDate fechaNacimiento,
                              String direccion, Long salarioBase) {
}