package com.vitalpets.laboratorio.service;

import com.vitalpets.laboratorio.dto.ExamenDto;
import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.model.ExamenLaboratorio;
import com.vitalpets.laboratorio.client.MascotaClient;
import com.vitalpets.laboratorio.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final MascotaClient mascotaClient;

    public ExamenDto solicitar(ExamenDto dto) {
        log.info("Solicitando examen: {} para mascota ID: {}",
                dto.getTipoExamen(), dto.getMascotaId());
        if (!mascotaClient.existeMascota(dto.getMascotaId())) {
            log.error("No se puede solicitar examen - Mascota ID: {} no encontrada", dto.getMascotaId());
            throw new RuntimeException("Mascota no encontrada con ID: " + dto.getMascotaId());
        }
        ExamenLaboratorio entidad = toEntity(dto);
        ExamenLaboratorio guardado = examenRepository.save(entidad);
        ExamenDto resultado = toDto(guardado);
        log.info("Examen solicitado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<ExamenDto> listarTodos() {
        log.info("Consultando todos los examenes de laboratorio");
        return examenRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExamenDto buscarPorId(Long id) {
        log.info("Buscando examen con ID: {}", id);
        ExamenLaboratorio e = examenRepository.findById(id).orElseThrow(() -> {
            log.error("Examen no encontrado con ID: {}", id);
            return new RuntimeException("Examen no encontrado: " + id);
        });
        return toDto(e);
    }

    public List<ExamenDto> porMascota(Long mascotaId) {
        log.info("Consultando examenes de mascota ID: {}", mascotaId);
        return examenRepository.findByMascotaId(mascotaId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ExamenDto> urgentes() {
        log.warn("Consultando examenes urgentes");
        return examenRepository.findByUrgenteTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExamenDto cargarResultado(Long id, String resultados, String observaciones) {
        log.info("Cargando resultado del examen ID: {}", id);
        ExamenLaboratorio e = examenRepository.findById(id).orElseThrow(() -> {
            log.error("Examen no encontrado para cargar resultado. ID: {}", id);
            return new RuntimeException("Examen no encontrado: " + id);
        });
        e.setResultados(resultados);
        e.setObservaciones(observaciones);
        e.setEstado(EstadoExamen.COMPLETADO);
        e.setFechaResultado(LocalDateTime.now());
        log.info("Resultado cargado exitosamente para examen ID: {}", id);
        return toDto(examenRepository.save(e));
    }

    public ExamenDto cambiarEstado(Long id, EstadoExamen estado) {
        log.info("Cambiando estado del examen ID: {} a {}", id, estado);
        ExamenLaboratorio e = examenRepository.findById(id).orElseThrow(() -> {
            log.error("Examen no encontrado para cambiar estado. ID: {}", id);
            return new RuntimeException("Examen no encontrado: " + id);
        });
        e.setEstado(estado);
        log.info("Estado del examen ID: {} actualizado a {}", id, estado);
        return toDto(examenRepository.save(e));
    }

    private ExamenDto toDto(ExamenLaboratorio e) {
        return ExamenDto.builder()
                .id(e.getId())
                .mascotaId(e.getMascotaId())
                .clienteId(e.getClienteId())
                .personalId(e.getPersonalId())
                .citaId(e.getCitaId())
                .tipoExamen(e.getTipoExamen())
                .estado(e.getEstado())
                .fechaSolicitud(e.getFechaSolicitud())
                .fechaResultado(e.getFechaResultado())
                .resultados(e.getResultados())
                .observaciones(e.getObservaciones())
                .urgente(e.getUrgente())
                .build();
    }

    private ExamenLaboratorio toEntity(ExamenDto dto) {
        return ExamenLaboratorio.builder()
                .mascotaId(dto.getMascotaId())
                .clienteId(dto.getClienteId())
                .personalId(dto.getPersonalId())
                .citaId(dto.getCitaId())
                .tipoExamen(dto.getTipoExamen())
                .urgente(dto.getUrgente() != null && dto.getUrgente())
                .build();
    }
}