package com.vitalpets.facturacion.controller;

import com.vitalpets.facturacion.dto.FacturaDto;
import com.vitalpets.facturacion.model.MetodoPago;
import com.vitalpets.facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Facturación", description = "Generación y gestión de facturas con desglose de servicios y productos")
public class FacturaController {

    private final FacturaService facturaService;

    @Operation(summary = "Crear nueva factura", description = "Genera una factura calculando totales automáticamente a partir de los detalles")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita asociada no encontrada")
    })
    @PostMapping
    public ResponseEntity<FacturaDto> crear(@RequestBody FacturaDto dto) {
        FacturaDto response = facturaService.crear(dto);
        response.add(linkTo(methodOn(FacturaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(FacturaController.class).listar()).withRel("facturas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas las facturas", description = "Retorna el historial completo de facturación con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<FacturaDto>> listar() {
        List<FacturaDto> lista = facturaService.listarTodas();
        lista.forEach(f -> f.add(
            linkTo(methodOn(FacturaController.class).buscarPorId(f.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(FacturaController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar factura por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Factura encontrada"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> buscarPorId(@PathVariable Long id) {
        FacturaDto response = facturaService.buscarPorId(id);
        response.add(linkTo(methodOn(FacturaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(FacturaController.class).listar()).withRel("facturas"));
        response.add(linkTo(methodOn(FacturaController.class).anular(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Facturas de un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.porCliente(clienteId));
    }

    @Operation(summary = "Registrar pago de factura", description = "Body: { \"metodoPago\": \"EFECTIVO\" }. Cambia estado a PAGADA")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago registrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<FacturaDto> pagar(@PathVariable Long id,
                                            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(facturaService.pagar(id,
                MetodoPago.valueOf(body.get("metodoPago"))));
    }

    @Operation(summary = "Anular factura (soft delete)", description = "Cambia el estado de la factura a ANULADA")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Factura anulada correctamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        facturaService.anular(id);
        return ResponseEntity.noContent().build();
    }
}
