package com.vitalpets.clientes.controller;

import com.vitalpets.clientes.dto.ClienteDto;
import com.vitalpets.clientes.dto.TerceroDto;
import com.vitalpets.clientes.service.ClienteService;
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
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dto) {
        ClienteDto response = clienteService.registrar(dto);
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ClienteController.class).listar()).withRel("clientes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ClienteDto>> listar() {
        List<ClienteDto> lista = clienteService.listarActivos();
        lista.forEach(c -> c.add(
            linkTo(methodOn(ClienteController.class).buscarPorId(c.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ClienteController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> buscarPorId(@PathVariable Long id) {
        ClienteDto response = clienteService.buscarPorId(id);
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ClienteController.class).listar()).withRel("clientes"));
        response.add(linkTo(methodOn(ClienteController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

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
