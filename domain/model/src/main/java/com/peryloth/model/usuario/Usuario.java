package com.peryloth.model.usuario;

import com.peryloth.model.rol.Rol;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

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
    private String passwordHash;
    private int intentosFallidos;
    private LocalDateTime bloqueadoHasta;

    public boolean puedeIntentarLogin() {
        return bloqueadoHasta == null || bloqueadoHasta.isBefore(LocalDateTime.now());
    }

    public void registrarIntentoFallido(int maxIntentos) {
        intentosFallidos++;
        if (intentosFallidos >= maxIntentos) {
            bloqueadoHasta = LocalDateTime.now().plusMinutes(15); // bloqueo temporal
            intentosFallidos = 0; // reinicia contador tras bloquear
        }
    }

    public void loginExitoso() {
        intentosFallidos = 0;
        bloqueadoHasta = null;
    }

    public boolean canUserDoing(Rol rol) {
        return rol.getNombre().equalsIgnoreCase(this.rol.getNombre());
    }
}
