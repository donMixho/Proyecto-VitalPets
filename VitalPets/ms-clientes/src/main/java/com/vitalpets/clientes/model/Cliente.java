package com.vitalpets.clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @NotBlank
    @Column(unique = true)
    private String numeroDocumento;

    @NotBlank
    private String telefono;

    private String email;
    private String direccion;

    @Builder.Default
    private Boolean activo = true;
}