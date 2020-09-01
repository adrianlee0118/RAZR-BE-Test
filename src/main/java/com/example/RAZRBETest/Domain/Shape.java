package com.example.RAZRBETest.Domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Shape {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;
    private int crossDim;
    private int area;

    public Shape(){ }

    public Shape(String type, int crossDim) {
        this.type = type;
        this.crossDim = crossDim;
        this.area = (int) Math.round(type == "Circle"? Math.PI*Math.pow(crossDim/2,2) : Math.pow(crossDim, 2));
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getCrossDim() {
        return crossDim;
    }

    public int getArea(){
        return area;
    }

    @Override
    public String toString(){
        return type+": "+(type == "Circle"? "Radius = "+Integer.toString(crossDim/2) : "Size = "+Integer.toString(crossDim))+", Area: "+area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Objects.equals(id, shape.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}