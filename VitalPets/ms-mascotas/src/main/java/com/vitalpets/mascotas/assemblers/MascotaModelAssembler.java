package com.vitalpets.mascotas.assemblers;

import com.vitalpets.mascotas.controller.MascotaController;
import com.vitalpets.mascotas.model.Mascota;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MascotaModelAssembler implements RepresentationModelAssembler<Mascota, EntityModel<Mascota>> {

    @Override
    public EntityModel<Mascota> toModel(Mascota mascota) {
        return EntityModel.of(mascota,
                linkTo(methodOn(MascotaController.class).buscarPorId(mascota.getId())).withSelfRel(),
                linkTo(methodOn(MascotaController.class).listarActivas()).withRel("mascotas"));
    }
}
