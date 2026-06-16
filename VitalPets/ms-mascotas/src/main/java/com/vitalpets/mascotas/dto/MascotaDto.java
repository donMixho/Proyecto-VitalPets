package com.vitalpets.mascotas.dto;

import com.vitalpets.mascotas.model.Especie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una mascota de compañía o exótica")
public class MascotaDto {

    @Schema(description = "Identificador único de la mascota", example = "1")
    private Long id;

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String nombre;

    @Schema(description = "Especie: PERRO, GATO, AVE, REPTIL, ROEDOR, EXÓTICO", example = "PERRO")
    private Especie especie;

    @Schema(description = "Raza de la mascota", example = "Labrador Retriever")
    private String raza;

    @Schema(description = "Edad en años completos", example = "3")
    private Integer edadAnios;

    @Schema(description = "Sexo de la mascota: MACHO o HEMBRA", example = "MACHO")
    private String sexo;

    @Schema(description = "Peso en kilogramos", example = "12.5")
    private Double pesoKg;

    @Schema(description = "Color del pelaje", example = "Dorado")
    private String colorPelaje;

    @Schema(description = "Notas especiales sobre la especie o cuidados", example = "Alérgico al maíz")
    private String notasEspecie;

    @Schema(description = "ID del cliente dueño de la mascota", example = "5")
    private Long clienteId;

    @Schema(description = "Indica si la mascota está activa en el sistema", example = "true")
    private Boolean activo;
}
