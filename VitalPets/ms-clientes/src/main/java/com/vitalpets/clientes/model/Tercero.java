package com.vitalpets.clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "terceros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String telefono;

    // Relación con el dueño legal que lo autorizó
    @Column(nullable = false)
    private Long clienteId;

    // Parentesco o relación (ej: "Familiar", "Vecino")
    private String relacion;

    @Builder.Default
    private Boolean activo = true;
}