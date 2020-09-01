package com.example.RAZRBETest.Bootstrap;

import com.example.RAZRBETest.Repositories.ShapeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//CommandLineRunner class run as soon as application context is loaded (when it "boots" i.e. "bootstrap")
@Component
public class Bootstrap implements CommandLineRunner {

    private final ShapeRepository shapeRepository;
    public Bootstrap(ShapeRepository shapeRepository) {this.shapeRepository = shapeRepository;}

    @Override
    public void run(String... args) throws Exception {
        //Pre-load data or other start-up operations here
    }
}
