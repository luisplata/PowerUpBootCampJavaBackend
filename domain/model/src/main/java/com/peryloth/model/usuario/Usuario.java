package com.peryloth.model.usuario;

import com.peryloth.model.rol.Rol;
import lombok.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {
    private BigInteger idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentidad;
    private String telefono;
    private Rol rol;
    private Long salarioBase;
}
