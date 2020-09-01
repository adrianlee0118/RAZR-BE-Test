package com.example.RAZRBETest.Controllers;

public class ShapeNotFoundException extends RuntimeException {

    ShapeNotFoundException(Long id) {
        super("Could not find shape with ID: "+id);
    }
}

