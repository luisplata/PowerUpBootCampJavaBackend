package com.peryloth.model.rol;

import lombok.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rol {
    private BigInteger uniqueId;
    private String nombre;
    private String descripcion;
}