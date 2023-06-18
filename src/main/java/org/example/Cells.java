package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    //eraseCell PENDING

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
        int column = n_coordinate.getNumColum();
        if (matrix.containsKey(row)) {
            Map<Integer, Cell> columns = matrix.get(row);
            if (columns.containsKey(column)) {
                return columns.get(column);
            }
        }
        // Create and return an empty Cell object for non-existent cells
        return new Cell();
    }


//    public Cell getCell(NumCoordinate n_coordinate) {
//        int row = n_coordinate.getNumRow();
//        int colum = n_coordinate.getNumColum();
//        if(matrix.containsKey(row)) {
//            Map<Integer, Cell> columns = matrix.get(row);
//            if(columns.containsKey(colum))
//                return columns.get(colum);
//        }
//        return new Cell(); //OJO aixi funciona en tots els casos?? Mirar prerequisists
//    }

    public NumCoordinate getSize() {
        Integer maxRow = 0;
        Integer maxCol = 0;
        for (Integer rowKey : matrix.keySet()) {
            for (Integer columnKey : matrix.get(rowKey).keySet()) {
                // Perform operations using rowKey and columnKey
                if (rowKey > maxRow) maxRow = rowKey;
                if (columnKey > maxCol) maxCol = columnKey;
                //System.out.println("Row key: " + rowKey + ", Column key: " + columnKey);
            }
        }
        return new NumCoordinate(maxRow, maxCol);
    }

    public Set<NumCoordinate> getCoordinateSet() {
        Set<NumCoordinate> coordinates = new HashSet<>();
        for (Integer rowKey : matrix.keySet()) {
            for (Integer columnKey : matrix.get(rowKey).keySet()) {
                // Perform operations using rowKey and columnKey
                NumCoordinate coordinate = new NumCoordinate(rowKey, columnKey);
                coordinates.add(coordinate);
                //System.out.println("Row key: " + rowKey + ", Column key: " + columnKey);
            }
        }
        return coordinates;
    }

    public void displayCells() {
        for (Integer rowKey : matrix.keySet()) {
            for (Integer columnKey : matrix.get(rowKey).keySet()) {
                Object object = matrix.get(rowKey).get(columnKey).getContent().getValue();
                System.out.println("Row: " + rowKey + ", Colum: "+ columnKey + " value: " + object);
            }
        }
    }
}
