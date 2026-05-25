package com.vitalpets.personal.service;

import com.vitalpets.personal.client.UsuarioClient;
import com.vitalpets.personal.dto.PersonalDto;
import com.vitalpets.personal.model.Especialidad;
import com.vitalpets.personal.model.Personal;
import com.vitalpets.personal.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalService {

    private final PersonalRepository personalRepository;
    private final UsuarioClient usuarioClient;

    public PersonalDto registrar(PersonalDto dto) {
        log.info("Registrando nuevo personal: {} {} - Especialidad: {}",
                dto.getNombre(), dto.getApellido(), dto.getEspecialidad());
        if (dto.getUsuarioId() != null && !usuarioClient.existeUsuario(dto.getUsuarioId())) {
            log.error("No se puede registrar personal - Usuario ID: {} no encontrado", dto.getUsuarioId());
            throw new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId());
        }
        PersonalDto resultado = toDto(personalRepository.save(toEntity(dto)));
        log.info("Personal registrado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<PersonalDto> listarActivos() {
        log.info("Consultando listado de personal activo");
        return personalRepository.findByActivoTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PersonalDto buscarPorId(Long id) {
        log.info("Buscando personal con ID: {}", id);
        return toDto(personalRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Personal no encontrado con ID: {}", id);
                    return new RuntimeException("Personal no encontrado: " + id);
                }));
    }

    public List<PersonalDto> buscarPorEspecialidad(Especialidad especialidad) {
        log.info("Buscando personal por especialidad: {}", especialidad);
        return personalRepository.findByEspecialidad(especialidad)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PersonalDto actualizarImplementos(Long id, String implementos) {
        log.info("Actualizando implementos del personal ID: {}", id);
        Personal p = personalRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Personal no encontrado para actualizar implementos. ID: {}", id);
                    return new RuntimeException("Personal no encontrado: " + id);
                });
        p.setImplementosAsignados(implementos);
        log.info("Implementos del personal ID: {} actualizados correctamente", id);
        return toDto(personalRepository.save(p));
    }

    public void desactivar(Long id) {
        log.warn("Desactivando personal con ID: {}", id);
        Personal p = personalRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Personal no encontrado para desactivar. ID: {}", id);
                    return new RuntimeException("Personal no encontrado: " + id);
                });
        p.setActivo(false);
        personalRepository.save(p);
        log.info("Personal ID: {} desactivado correctamente", id);
    }

    private PersonalDto toDto(Personal p) {
        return PersonalDto.builder()
                .id(p.getId()).nombre(p.getNombre()).apellido(p.getApellido())
                .rut(p.getRut()).telefono(p.getTelefono()).email(p.getEmail())
                .profesion(p.getProfesion()).especialidad(p.getEspecialidad())
                .implementosAsignados(p.getImplementosAsignados())
                .activo(p.getActivo()).build();
    }

    private Personal toEntity(PersonalDto dto) {
        return Personal.builder()
                .nombre(dto.getNombre()).apellido(dto.getApellido())
                .rut(dto.getRut()).telefono(dto.getTelefono()).email(dto.getEmail())
                .profesion(dto.getProfesion()).especialidad(dto.getEspecialidad())
                .implementosAsignados(dto.getImplementosAsignados()).build();
    }
}