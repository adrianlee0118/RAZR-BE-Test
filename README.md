## About
RESTf API with hypermedia-driven support for CRUD calls that generates data for creating randomly-sized squares and circles written in Java (Spring-Boot)


![Demo](https://github.com/adrianlee0118/RAZR-BE-Test/blob/master/assets/demo.gif)


## Install

- Clone repo
- Run ```mvnw spring-boot:run``` in root directory
- Head to ```localhost:8080``` and use the input field to query endpoints or change headers

## Endpoints

- "/shapes/new" to generate and get a new set of 50 randomly sized circles and 50 randomly sized squares
- "/shapes/all" to get the most recently generated set of shapes in increasing order of ID
- "/shapes/sorted" to get the most recently generated set of shapes by decreasing order of area
- "/shapes/{id}" to get a single shape by its ID
- "/shapes/meanArea" to get the shape(s) with area closest to the mean of the most recently generated set of 100 shapes
