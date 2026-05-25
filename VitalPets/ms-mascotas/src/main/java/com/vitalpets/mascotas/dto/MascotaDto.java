package com.vitalpets.mascotas.dto;

import com.vitalpets.mascotas.model.Especie;
import lombok.*;

// DTO: objeto que controla qué datos entran y salen por la API.
// Nunca exponemos la entidad directamente al cliente.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaDto {

    private Long id;
    private String nombre;
    private Especie especie;
    private String raza;
    private Integer edadAnios;
    private String sexo;
    private Double pesoKg;
    private String colorPelaje;
    private String notasEspecie;
    private Long clienteId;
    private Boolean activo;
}