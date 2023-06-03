package org.example;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NumCoordinate otherNumCoordinate = (NumCoordinate) obj;
        return numColum == otherNumCoordinate.numColum && numRow == otherNumCoordinate.numRow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numColum, numRow);
    }

    @Override
    public String toString() {
        return "(" + numColum + ", " + numRow + ")";
    }
}
