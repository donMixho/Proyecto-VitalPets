package com.vitalpets.clientes.controller;

import com.vitalpets.clientes.dto.ClienteDto;
import com.vitalpets.clientes.dto.TerceroDto;
import com.vitalpets.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar() {
        return ResponseEntity.ok(clienteService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Terceros ──────────────────────────────────────────────
    @PostMapping("/terceros")
    public ResponseEntity<TerceroDto> registrarTercero(@RequestBody TerceroDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.registrarTercero(dto));
    }

    @GetMapping("/{clienteId}/terceros")
    public ResponseEntity<List<TerceroDto>> tercerosPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(clienteService.tercerosPorCliente(clienteId));
    }
}