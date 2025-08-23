package com.peryloth.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Table("usuario")
public class UsuarioEntity {
    @Id
    private BigInteger id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;
    private String direccion;
    private Long salarioBase;
}
