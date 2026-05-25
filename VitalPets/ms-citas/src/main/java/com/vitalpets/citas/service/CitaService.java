package com.vitalpets.citas.service;

import com.vitalpets.citas.client.ClienteClient;
import com.vitalpets.citas.client.MascotaClient;
import com.vitalpets.citas.dto.CitaDto;
import com.vitalpets.citas.model.Cita;
import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaClient mascotaClient;
    private final ClienteClient clienteClient;

    public CitaDto registrar(CitaDto dto) {
        log.info("Registrando nueva cita para mascota ID: {} - Servicio: {}",
                dto.getMascotaId(), dto.getTipoServicio());

        // Verificar que la mascota existe en MS-Mascotas
        if (!mascotaClient.existeMascota(dto.getMascotaId())) {
            log.error("No se puede agendar cita - Mascota ID: {} no encontrada en MS-Mascotas",
                    dto.getMascotaId());
            throw new RuntimeException("Mascota no encontrada con ID: " + dto.getMascotaId());
        }

        // Verificar que el cliente existe en MS-Clientes
        if (!clienteClient.existeCliente(dto.getClienteId())) {
            log.error("No se puede agendar cita - Cliente ID: {} no encontrado en MS-Clientes",
                    dto.getClienteId());
            throw new RuntimeException("Cliente no encontrado con ID: " + dto.getClienteId());
        }

        CitaDto resultado = toDto(citaRepository.save(toEntity(dto)));
        log.info("Cita registrada exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<CitaDto> listarTodas() {
        log.info("Consultando todas las citas");
        return citaRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public CitaDto buscarPorId(Long id) {
        log.info("Buscando cita con ID: {}", id);
        return toDto(citaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cita no encontrada con ID: {}", id);
                    return new RuntimeException("Cita no encontrada: " + id);
                }));
    }

    public List<CitaDto> porCliente(Long clienteId) {
        log.info("Consultando citas del cliente ID: {}", clienteId);
        return citaRepository.findByClienteId(clienteId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<CitaDto> porEstado(EstadoCita estado) {
        log.info("Consultando citas por estado: {}", estado);
        return citaRepository.findByEstado(estado)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public CitaDto cambiarEstado(Long id, EstadoCita nuevoEstado) {
        log.info("Cambiando estado de cita ID: {} a {}", id, nuevoEstado);
        Cita c = citaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cita no encontrada para cambiar estado. ID: {}", id);
                    return new RuntimeException("Cita no encontrada: " + id);
                });
        c.setEstado(nuevoEstado);
        log.info("Estado de cita ID: {} actualizado a {}", id, nuevoEstado);
        return toDto(citaRepository.save(c));
    }

    public void cancelar(Long id) {
        log.warn("Cancelando cita con ID: {}", id);
        cambiarEstado(id, EstadoCita.CANCELADA);
        log.info("Cita ID: {} cancelada correctamente", id);
    }

    private CitaDto toDto(Cita c) {
        return CitaDto.builder()
                .id(c.getId())
                .mascotaId(c.getMascotaId())
                .clienteId(c.getClienteId())
                .terceroId(c.getTerceroId())
                .personalId(c.getPersonalId())
                .fechaHora(c.getFechaHora())
                .tipoServicio(c.getTipoServicio())
                .estado(c.getEstado())
                .observaciones(c.getObservaciones())
                .nombreQuienTrae(c.getNombreQuienTrae())
                .build();
    }

    private Cita toEntity(CitaDto dto) {
        return Cita.builder()
                .mascotaId(dto.getMascotaId())
                .clienteId(dto.getClienteId())
                .terceroId(dto.getTerceroId())
                .personalId(dto.getPersonalId())
                .fechaHora(dto.getFechaHora())
                .tipoServicio(dto.getTipoServicio())
                .observaciones(dto.getObservaciones())
                .nombreQuienTrae(dto.getNombreQuienTrae())
                .build();
    }
}