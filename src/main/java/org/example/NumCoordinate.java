package org.example;

public class NumCoordinate {
    private Integer numColum;
    private Integer numRow;

    public NumCoordinate() {

    }

    public NumCoordinate(Integer numRow, Integer num_column) {
        this.numColum = num_column;
        this.numRow = numRow;
    }

    public void setNumColum(Integer numColum) {
        this.numColum = numColum;
    }

    public Integer getNumColum() {
        return this.numColum;
    }

    public void setNumRow(Integer numRow) {
        this.numRow = numRow;
    }

    public Integer getNumRow() {
        return this.numRow;
    }

    public void display_coordinate() {
        System.out.println("coordinate: colum: " + numColum + " row: " + numRow);
    }
}
