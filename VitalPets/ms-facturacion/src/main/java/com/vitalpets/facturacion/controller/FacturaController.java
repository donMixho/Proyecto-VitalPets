package com.vitalpets.facturacion.controller;

import com.vitalpets.facturacion.dto.FacturaDto;
import com.vitalpets.facturacion.model.MetodoPago;
import com.vitalpets.facturacion.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaDto> crear(@RequestBody FacturaDto dto) {
        FacturaDto response = facturaService.crear(dto);
        response.add(linkTo(methodOn(FacturaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(FacturaController.class).listar()).withRel("facturas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<FacturaDto>> listar() {
        List<FacturaDto> lista = facturaService.listarTodas();
        lista.forEach(f -> f.add(
            linkTo(methodOn(FacturaController.class).buscarPorId(f.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(FacturaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> buscarPorId(@PathVariable Long id) {
        FacturaDto response = facturaService.buscarPorId(id);
        response.add(linkTo(methodOn(FacturaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(FacturaController.class).listar()).withRel("facturas"));
        response.add(linkTo(methodOn(FacturaController.class).anular(id)).withRel("delete"));
        return ResponseEntity.ok(response);
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
