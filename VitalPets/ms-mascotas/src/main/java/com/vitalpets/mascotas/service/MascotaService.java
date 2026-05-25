package com.vitalpets.mascotas.service;

import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaDto registrar(MascotaDto dto) {
        log.info("Registrando nueva mascota: {} - Especie: {}", dto.getNombre(), dto.getEspecie());
        MascotaDto resultado = toDto(mascotaRepository.save(toEntity(dto)));
        log.info("Mascota registrada exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<MascotaDto> listarActivas() {
        log.info("Consultando listado de mascotas activas");
        return mascotaRepository.findByActivoTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public MascotaDto buscarPorId(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mascota no encontrada con ID: {}", id);
                    return new RuntimeException("Mascota no encontrada con ID: " + id);
                });
        return toDto(mascota);
    }

    public List<MascotaDto> buscarPorCliente(Long clienteId) {
        log.info("Buscando mascotas del cliente ID: {}", clienteId);
        return mascotaRepository.findByClienteId(clienteId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MascotaDto> buscarPorEspecie(Especie especie) {
        log.info("Buscando mascotas por especie: {}", especie);
        return mascotaRepository.findByEspecie(especie)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public MascotaDto actualizar(Long id, MascotaDto dto) {
        log.info("Actualizando mascota con ID: {}", id);
        Mascota existente = mascotaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mascota no encontrada para actualizar. ID: {}", id);
                    return new RuntimeException("Mascota no encontrada con ID: " + id);
                });
        existente.setNombre(dto.getNombre());
        existente.setRaza(dto.getRaza());
        existente.setEdadAnios(dto.getEdadAnios());
        existente.setPesoKg(dto.getPesoKg());
        existente.setNotasEspecie(dto.getNotasEspecie());
        log.info("Mascota ID: {} actualizada correctamente", id);
        return toDto(mascotaRepository.save(existente));
    }

    public void desactivar(Long id) {
        log.warn("Desactivando mascota con ID: {}", id);
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mascota no encontrada para desactivar. ID: {}", id);
                    return new RuntimeException("Mascota no encontrada con ID: " + id);
                });
        mascota.setActivo(false);
        mascotaRepository.save(mascota);
        log.info("Mascota ID: {} desactivada correctamente", id);
    }

    private MascotaDto toDto(Mascota m) {
        return MascotaDto.builder()
                .id(m.getId()).nombre(m.getNombre()).especie(m.getEspecie())
                .raza(m.getRaza()).edadAnios(m.getEdadAnios()).sexo(m.getSexo())
                .pesoKg(m.getPesoKg()).colorPelaje(m.getColorPelaje())
                .notasEspecie(m.getNotasEspecie()).clienteId(m.getClienteId())
                .activo(m.getActivo()).build();
    }

    private Mascota toEntity(MascotaDto dto) {
        return Mascota.builder()
                .nombre(dto.getNombre()).especie(dto.getEspecie())
                .raza(dto.getRaza()).edadAnios(dto.getEdadAnios()).sexo(dto.getSexo())
                .pesoKg(dto.getPesoKg()).colorPelaje(dto.getColorPelaje())
                .notasEspecie(dto.getNotasEspecie()).clienteId(dto.getClienteId())
                .build();
    }
}