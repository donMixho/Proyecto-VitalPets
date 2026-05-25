package com.vitalpets.vacunas.service;

import com.vitalpets.vacunas.dto.VacunaDto;
import com.vitalpets.vacunas.model.Vacuna;
import com.vitalpets.vacunas.client.MascotaClient;
import com.vitalpets.vacunas.repository.VacunaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacunaService {

    private final VacunaRepository vacunaRepository;
    private final MascotaClient mascotaClient;

    public VacunaDto registrar(VacunaDto dto) {
        log.info("Registrando vacuna: {} para mascota ID: {}",
                dto.getNombreVacuna(), dto.getMascotaId());
        if (!mascotaClient.existeMascota(dto.getMascotaId())) {
            log.error("No se puede registrar vacuna - Mascota ID: {} no encontrada", dto.getMascotaId());
            throw new RuntimeException("Mascota no encontrada con ID: " + dto.getMascotaId());
        }
        Vacuna entidad = toEntity(dto);
        Vacuna guardada = vacunaRepository.save(entidad);
        VacunaDto resultado = toDto(guardada);
        log.info("Vacuna registrada exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<VacunaDto> listarVigentes() {
        log.info("Consultando vacunas vigentes");
        return vacunaRepository.findByVigenteTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<VacunaDto> porMascota(Long mascotaId) {
        log.info("Consultando vacunas de mascota ID: {}", mascotaId);
        return vacunaRepository.findByMascotaId(mascotaId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public VacunaDto buscarPorId(Long id) {
        log.info("Buscando vacuna con ID: {}", id);
        Vacuna v = vacunaRepository.findById(id).orElseThrow(() -> {
            log.error("Vacuna no encontrada con ID: {}", id);
            return new RuntimeException("Vacuna no encontrada: " + id);
        });
        return toDto(v);
    }

    public List<VacunaDto> proximasAVencer() {
        log.warn("Consultando vacunas proximas a vencer en los proximos 30 dias");
        List<VacunaDto> proximas = vacunaRepository
                .findByFechaProximaDosisBeforeAndVigenteTrue(LocalDate.now().plusDays(30))
                .stream().map(this::toDto).collect(Collectors.toList());
        log.info("Se encontraron {} vacunas proximas a vencer", proximas.size());
        return proximas;
    }

    private VacunaDto toDto(Vacuna v) {
        return VacunaDto.builder()
                .id(v.getId())
                .mascotaId(v.getMascotaId())
                .clienteId(v.getClienteId())
                .personalId(v.getPersonalId())
                .nombreVacuna(v.getNombreVacuna())
                .laboratorio(v.getLaboratorio())
                .lote(v.getLote())
                .fechaAplicacion(v.getFechaAplicacion())
                .fechaProximaDosis(v.getFechaProximaDosis())
                .dosis(v.getDosis())
                .observaciones(v.getObservaciones())
                .vigente(v.getVigente())
                .build();
    }

    private Vacuna toEntity(VacunaDto dto) {
        return Vacuna.builder()
                .mascotaId(dto.getMascotaId())
                .clienteId(dto.getClienteId())
                .personalId(dto.getPersonalId())
                .nombreVacuna(dto.getNombreVacuna())
                .laboratorio(dto.getLaboratorio())
                .lote(dto.getLote())
                .fechaAplicacion(dto.getFechaAplicacion())
                .fechaProximaDosis(dto.getFechaProximaDosis())
                .dosis(dto.getDosis())
                .observaciones(dto.getObservaciones())
                .build();
    }
}