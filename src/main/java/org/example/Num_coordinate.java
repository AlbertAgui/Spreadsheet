package org.example;

public class Num_coordinate {
    public Integer num_column;
    public Integer num_row;

    public Num_coordinate() {

    }

    public Num_coordinate(Integer num_row, Integer num_column) {
        this.num_column = num_column;
        this.num_row = num_row;
    }

    public void display_coordinate() {
        System.out.println("coordinate: colum: " + num_column + " row: " + num_row);
    }
}
