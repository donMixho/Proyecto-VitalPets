package com.vitalpets.mascotas.service;

import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public Mascota registrar(MascotaDto dto) {
        log.info("Registrando nueva mascota: {} - Especie: {}", dto.getNombre(), dto.getEspecie());
        Mascota resultado = mascotaRepository.save(toEntity(dto));
        log.info("Mascota registrada exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<Mascota> listarActivas() {
        log.info("Consultando listado de mascotas activas");
        return mascotaRepository.findByActivoTrue();
    }

    public Mascota buscarPorId(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        return mascotaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mascota no encontrada con ID: {}", id);
                    return new RuntimeException("Mascota no encontrada con ID: " + id);
                });
    }

    public List<Mascota> buscarPorCliente(Long clienteId) {
        log.info("Buscando mascotas del cliente ID: {}", clienteId);
        return mascotaRepository.findByClienteId(clienteId);
    }

    public List<Mascota> buscarPorEspecie(Especie especie) {
        log.info("Buscando mascotas por especie: {}", especie);
        return mascotaRepository.findByEspecie(especie);
    }

    public Mascota actualizar(Long id, MascotaDto dto) {
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
        return mascotaRepository.save(existente);
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

    private Mascota toEntity(MascotaDto dto) {
        return Mascota.builder()
                .nombre(dto.getNombre()).especie(dto.getEspecie())
                .raza(dto.getRaza()).edadAnios(dto.getEdadAnios()).sexo(dto.getSexo())
                .pesoKg(dto.getPesoKg()).colorPelaje(dto.getColorPelaje())
                .notasEspecie(dto.getNotasEspecie()).clienteId(dto.getClienteId())
                .build();
    }
}
