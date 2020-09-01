package com.example.RAZRBETest.Controllers;

import com.example.RAZRBETest.Domain.Shape;
import com.example.RAZRBETest.Repositories.ShapeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;

@RestController
public class ShapeController {

    private final ShapeRepository shapeRepository;
    private final ShapeModelAssembler assembler;
    private int meanArea;

    public ShapeController(ShapeRepository shapeRepository, ShapeModelAssembler assembler) {
        this.shapeRepository = shapeRepository;
        this.assembler = assembler;
    }

    //Gets existing list of shapes with links to various items and the full list
    @GetMapping("/shapes/all")
    CollectionModel<EntityModel<Shape>> all() {
        List<EntityModel<Shape>> shapes = StreamSupport.stream(shapeRepository.findAll().spliterator(),false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(shapes, linkTo(methodOn(ShapeController.class).all()).withSelfRel());
    }

    //Gets a new random list of 50 circles and squares that overwrites previous shapes
    @GetMapping("/shapes/new")
    CollectionModel<EntityModel<Shape>> newSet(){
        shapeRepository.deleteAll();
        int i = 1, areaSum = 0;
        while (i <= 50){
            Shape circle = new Shape("Circle", ThreadLocalRandom.current().nextInt(1,101));
            areaSum += circle.getArea();
            shapeRepository.save(circle);
            i++;
        }
        while (i <= 100){
            Shape square = new Shape("Square", ThreadLocalRandom.current().nextInt(1, 101));
            areaSum += square.getArea();
            shapeRepository.save(square);
            i++;
        }
        meanArea = Math.round(areaSum/100);
        return this.all();
    }

    //Sorts the list of shapes by area in descending order, as per Comparable implemented in Shape class
    @GetMapping("/shapes/sorted")
    CollectionModel<EntityModel<Shape>> sort() {
        List<EntityModel<Shape>> shapes = StreamSupport.stream(shapeRepository.findAll().spliterator(),false)
                .map(assembler::toModel)
                .sorted((shape1, shape2) -> shape2.getContent().getArea() - shape1.getContent().getArea())
                .collect(Collectors.toList());
        return CollectionModel.of(shapes, linkTo(methodOn(ShapeController.class).all()).withSelfRel());
    }

    // Returns the shape or set of shapes that have an area closest to the mean area of the cohort
    @GetMapping("/shapes/meanArea")
    CollectionModel<EntityModel<Shape>> getMeanAreaShapes(){
        HashMap<Integer, List<Shape>> diffShapes = new HashMap<Integer, List<Shape>>();
        int minDiff = 10000;
        for (Shape shape : this.shapeRepository.findAll()){
            int diff = Math.abs(this.meanArea - shape.getArea());
            List<Shape> group = (diffShapes.get(diff) != null)? diffShapes.get(diff) : new ArrayList<Shape>();
            group.add(shape);
            diffShapes.put(diff,group);
            if (diff < minDiff) minDiff = diff;
        }
        List<EntityModel<Shape>> ans = new ArrayList<EntityModel<Shape>>();
        diffShapes.get(minDiff).forEach(shape -> ans.add(assembler.toModel(shape)));
        return CollectionModel.of(ans, linkTo(methodOn(ShapeController.class).all()).withSelfRel());
    }

    // Return one shape by requested ID with links to shape itself and all shapes
    @GetMapping("/shapes/{id}")
    EntityModel<Shape> one(@PathVariable Long id) {
        Shape shape = shapeRepository.findById(id)
                .orElseThrow(() -> new ShapeNotFoundException(id));
        return assembler.toModel(shape);
    }

    //Add a shape - ResponseEntity wrapper creates "HTTP 201 Created" status message
    @PostMapping("/shapes")
    ResponseEntity<?> newShape(@RequestBody Shape newShape) {
        EntityModel<Shape> entityModel = assembler.toModel(shapeRepository.save(newShape));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    //Update a shape of a certain ID if it exists, otherwise create a new shape with the passed ID
    @PutMapping("/shapes/{id}")
    ResponseEntity<?> replaceShape(@RequestBody Shape newShape, @PathVariable Long id) {

        Shape updatedShape = shapeRepository.findById(id)
                .map(shape -> {
                    shape.setType(newShape.getType());
                    shape.setCrossDim(newShape.getCrossDim());
                    shape.setArea(newShape.getArea());
                    return shapeRepository.save(shape);
                })
                .orElseGet(() -> {
                    newShape.setId(id);
                    return shapeRepository.save(newShape);
                });

        EntityModel<Shape> entityModel = assembler.toModel(updatedShape);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    //Delete shapes by Id
    @DeleteMapping("/shapes/{id}")
    ResponseEntity<?> deleteShape(@PathVariable Long id) {
        shapeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
