package com.vitalpets.clientes.controller;

import com.vitalpets.clientes.dto.ClienteDto;
import com.vitalpets.clientes.dto.TerceroDto;
import com.vitalpets.clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gestión de dueños legales de mascotas y terceros autorizados")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Registrar nuevo cliente", description = "Crea un dueño legal de mascota en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dto) {
        ClienteDto response = clienteService.registrar(dto);
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ClienteController.class).listar()).withRel("clientes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar clientes activos", description = "Retorna todos los clientes con activo=true y enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<ClienteDto>> listar() {
        List<ClienteDto> lista = clienteService.listarActivos();
        lista.forEach(c -> c.add(
            linkTo(methodOn(ClienteController.class).buscarPorId(c.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ClienteController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long id) {
        ClienteDto response = clienteService.buscarPorId(id);
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ClienteController.class).listar()).withRel("clientes"));
        response.add(linkTo(methodOn(ClienteController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desactivar cliente (soft delete)", description = "Marca el cliente como inactivo sin eliminarlo de la base de datos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente desactivado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Registrar tercero autorizado", description = "Agrega una persona autorizada para retirar la mascota en nombre del cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tercero registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/terceros")
    public ResponseEntity<TerceroDto> registrarTercero(@RequestBody TerceroDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.registrarTercero(dto));
    }

    @Operation(summary = "Listar terceros autorizados de un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/{clienteId}/terceros")
    public ResponseEntity<List<TerceroDto>> tercerosPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(clienteService.tercerosPorCliente(clienteId));
    }
}
