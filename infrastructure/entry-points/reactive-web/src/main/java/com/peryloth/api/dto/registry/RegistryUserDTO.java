package com.peryloth.api.dto.registry;

import java.math.BigInteger;
import java.time.LocalDate;

public record RegistryUserDTO(BigInteger id, String nombre, String apellido, String email, LocalDate fechaNacimiento,
                              String direccion, Long salarioBase) {
}