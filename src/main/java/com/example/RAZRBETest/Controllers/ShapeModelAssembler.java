package com.example.RAZRBETest.Controllers;

import com.example.RAZRBETest.Domain.Shape;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ShapeModelAssembler implements RepresentationModelAssembler<Shape, EntityModel<Shape>> {

    @Override
    public EntityModel<Shape> toModel(Shape shape) {
        return EntityModel.of(shape,
                linkTo(methodOn(ShapeController.class).one(shape.getId())).withSelfRel(),
                linkTo(methodOn(ShapeController.class).all()).withRel("/shapes/all"));
    }
}
