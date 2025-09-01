package com.peryloth.model.rol;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void crearRolConAllArgsConstructor() {
        String name = "ADMIN";
        BigInteger id = BigInteger.ONE;
        String description = "Administrador del sistema";
        Rol rol = new Rol(id, name, description);

        assertNotNull(rol);
        assertEquals(id, rol.getUniqueId());
        assertEquals(name, rol.getNombre());
        assertEquals(description, rol.getDescripcion());
    }

    @Test
    void crearRolConBuilder() {

        String name = "USER";
        BigInteger id = BigInteger.TWO;
        String description = "Usuario estándar";

        Rol rol = Rol.builder()
                .uniqueId(id)
                .nombre(name)
                .descripcion(description)
                .build();

        assertNotNull(rol);
        assertEquals(name, rol.getNombre());
        assertEquals(description, rol.getDescripcion());
    }

    @Test
    void modificarRolConSetters() {

        String name = "MANAGER";
        String description = "Gestor de área";

        Rol rol = new Rol();
        rol.setNombre(name);
        rol.setDescripcion(description);

        assertEquals(name, rol.getNombre());
        assertEquals(description, rol.getDescripcion());
    }

    @Test
    void usarToBuilderParaModificar() {

        String name = "ADMIN";
        BigInteger id = BigInteger.TWO;
        String description = "Administrador";

        String nameEdited = "SUPERADMIN";

        Rol rol = Rol.builder()
                .uniqueId(id)
                .nombre(name)
                .descripcion(description)
                .build();

        Rol rolModificado = rol.toBuilder()
                .nombre(nameEdited)
                .build();

        assertEquals(nameEdited, rolModificado.getNombre());
        assertEquals(description, rolModificado.getDescripcion()); // mantiene la descripción
        assertEquals(id, rolModificado.getUniqueId()); // mantiene el id
    }
}
