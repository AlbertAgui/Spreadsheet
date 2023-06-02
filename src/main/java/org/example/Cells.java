package org.example;

import java.util.HashMap;
import java.util.Map;

public class Cells {
    private Map<Integer, Map<Integer, Cell>> matrix;

    public Cells(){
        matrix = new HashMap<>();
    }

    public Boolean containsCell(NumCoordinate n_coordinate) {
        int row = n_coordinate.getNumRow();
        int colum = n_coordinate.getNumColum();
        if(matrix.containsKey(row)) {
            Map<Integer, Cell> columns = matrix.get(row);
            if(columns.containsKey(colum)) {
                return true;
            }
        }
        return false;
    }

    public void addCell(NumCoordinate n_coordinate, Cell cell) {
        int row = n_coordinate.getNumRow();
        int colum = n_coordinate.getNumColum();
        //System.out.println("Cells: colum: " + colum + " row: " + row + " value: " + cell.getValue() + "\n");
        if(!matrix.containsKey(row)) {
            matrix.put(row, new HashMap<>());
        }
        matrix.get(row).put(colum, cell);
    }

    public Cell getCell(NumCoordinate n_coordinate) {
        int row = n_coordinate.getNumRow();
        int colum = n_coordinate.getNumColum();
        if(matrix.containsKey(row)) {
            Map<Integer, Cell> columns = matrix.get(row);
            if(columns.containsKey(colum))
                return columns.get(colum);
        }
        return null;
    }

    public void displayCells() {
        Integer nRow = 1;
        for (Map<Integer, Cell> innerMap : matrix.values()) {
            Integer nColum = 1;
            for (Cell cell : innerMap.values()) {
                System.out.println("Colum: "+ nColum + " Row: " + nRow + " value: " + cell.getValue());
                ++nColum;
            }
            ++nRow;
        }
    }
}
