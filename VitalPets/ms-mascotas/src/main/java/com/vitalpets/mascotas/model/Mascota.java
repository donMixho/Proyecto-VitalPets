package com.vitalpets.mascotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Data                  // Lombok: genera getters, setters, toString
@NoArgsConstructor     // Lombok: constructor vacío (requerido por JPA)
@AllArgsConstructor    // Lombok: constructor con todos los campos
@Builder               // Lombok: patrón builder para crear objetos
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    @Enumerated(EnumType.STRING) // Guarda "PERRO" en la BD, no un número
    @Column(nullable = false)
    private Especie especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edadAnios;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo; // "MACHO" o "HEMBRA"

    private Double pesoKg;

    private String colorPelaje;

    // Notas especiales según especie (ej: "requiere calor constante" para reptil)
    @Column(length = 500)
    private String notasEspecie;

    // ID del dueño legal — viene del MS-Clientes (8082)
    @Column(nullable = false)
    private Long clienteId;

    // Estado activo/inactivo en el sistema
    @Column(nullable = false)
    @Builder.Default  //en caso de que coloquen un valor vacio sea true por defecto
    private Boolean activo = true;
}