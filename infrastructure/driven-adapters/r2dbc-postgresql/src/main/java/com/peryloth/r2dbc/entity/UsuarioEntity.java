package com.peryloth.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Table("usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {
    @Id
    @Column("id_usuario")
    private BigInteger idUsuario;

    private String nombre;
    private String apellido;
    private String email;

    @Column("documento_identidad")
    private String documentoIdentidad;

    private String telefono;

    @Column("id_rol")
    private BigInteger rolId;

    @Column("salario_base")
    private Long salarioBase;

    @Column("password_hash")
    private String passwordHash;
}
