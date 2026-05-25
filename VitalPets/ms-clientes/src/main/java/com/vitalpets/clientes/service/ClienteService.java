package com.vitalpets.clientes.service;

import com.vitalpets.clientes.dto.ClienteDto;
import com.vitalpets.clientes.dto.TerceroDto;
import com.vitalpets.clientes.model.Cliente;
import com.vitalpets.clientes.model.Tercero;
import com.vitalpets.clientes.repository.ClienteRepository;
import com.vitalpets.clientes.repository.TerceroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TerceroRepository terceroRepository;

    public ClienteDto registrar(ClienteDto dto) {
        log.info("Registrando nuevo cliente: {} {}", dto.getNombre(), dto.getApellido());
        ClienteDto resultado = toDto(clienteRepository.save(toEntity(dto)));
        log.info("Cliente registrado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<ClienteDto> listarActivos() {
        log.info("Consultando listado de clientes activos");
        return clienteRepository.findByActivoTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ClienteDto buscarPorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);
        return toDto(clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado con ID: {}", id);
                    return new RuntimeException("Cliente no encontrado: " + id);
                }));
    }

    public void desactivar(Long id) {
        log.warn("Desactivando cliente con ID: {}", id);
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado para desactivar. ID: {}", id);
                    return new RuntimeException("Cliente no encontrado: " + id);
                });
        c.setActivo(false);
        clienteRepository.save(c);
        log.info("Cliente ID: {} desactivado correctamente", id);
    }

    public TerceroDto registrarTercero(TerceroDto dto) {
        log.info("Registrando tercero autorizado: {} {} para cliente ID: {}",
                dto.getNombre(), dto.getApellido(), dto.getClienteId());
        Tercero t = Tercero.builder()
                .nombre(dto.getNombre()).apellido(dto.getApellido())
                .telefono(dto.getTelefono()).clienteId(dto.getClienteId())
                .relacion(dto.getRelacion()).build();
        TerceroDto resultado = toTerceroDto(terceroRepository.save(t));
        log.info("Tercero registrado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<TerceroDto> tercerosPorCliente(Long clienteId) {
        log.info("Consultando terceros autorizados del cliente ID: {}", clienteId);
        return terceroRepository.findByClienteId(clienteId)
                .stream().map(this::toTerceroDto).collect(Collectors.toList());
    }

    private ClienteDto toDto(Cliente c) {
        return ClienteDto.builder()
                .id(c.getId()).nombre(c.getNombre()).apellido(c.getApellido())
                .tipoDocumento(c.getTipoDocumento()).numeroDocumento(c.getNumeroDocumento())
                .telefono(c.getTelefono()).email(c.getEmail())
                .direccion(c.getDireccion()).activo(c.getActivo()).build();
    }

    private Cliente toEntity(ClienteDto dto) {
        return Cliente.builder()
                .nombre(dto.getNombre()).apellido(dto.getApellido())
                .tipoDocumento(dto.getTipoDocumento()).numeroDocumento(dto.getNumeroDocumento())
                .telefono(dto.getTelefono()).email(dto.getEmail())
                .direccion(dto.getDireccion()).build();
    }

    private TerceroDto toTerceroDto(Tercero t) {
        return TerceroDto.builder()
                .id(t.getId()).nombre(t.getNombre()).apellido(t.getApellido())
                .telefono(t.getTelefono()).clienteId(t.getClienteId())
                .relacion(t.getRelacion()).activo(t.getActivo()).build();
    }
}