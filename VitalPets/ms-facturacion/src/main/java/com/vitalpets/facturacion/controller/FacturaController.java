package com.vitalpets.facturacion.controller;

import com.vitalpets.facturacion.dto.FacturaDto;
import com.vitalpets.facturacion.model.MetodoPago;
import com.vitalpets.facturacion.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaDto> crear(@RequestBody FacturaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<FacturaDto>> listar() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.porCliente(clienteId));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<FacturaDto> pagar(@PathVariable Long id,
                                            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(facturaService.pagar(id,
                MetodoPago.valueOf(body.get("metodoPago"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        facturaService.anular(id);
        return ResponseEntity.noContent().build();
    }
}