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

    public void addCell(NumCoordinate numCoordinate, Cell cell) {
        int row = numCoordinate.getNumRow();
        int colum = numCoordinate.getNumColum();
        //System.out.println("Cells: colum: " + colum + " row: " + row + " value: " + cell.getValue() + "\n");
        if (!matrix.containsKey(row)) {
            matrix.put(row, new HashMap<>());
        }
        matrix.get(row).put(colum, cell);
    }

    public void eraseCell(NumCoordinate numCoordinate) { //ROWS ARE NOT ERASED
        int row = numCoordinate.getNumRow();
        int colum = numCoordinate.getNumColum();
        if(matrix.containsKey(row)) {
            Map<Integer, Cell> columns = matrix.get(row);
            if(columns.containsKey(colum)) {
                matrix.get(row).remove(colum);
            } else {
                System.out.println("Cell not found");
            }
        } else {
            System.out.println("Cell not found");
        }
    }

    public Cell getCell(NumCoordinate numCoordinate)  {
        int row = numCoordinate.getNumRow();
        int colum = numCoordinate.getNumColum();
        if(matrix.containsKey(row)) {
            Map<Integer, Cell> columns = matrix.get(row);
            if(columns.containsKey(colum))
                return columns.get(colum);
        }
        return null;
    }

    public NumCoordinate getSize() {
        Integer maxRow = 0;
        Integer maxCol = 0;
        for (Integer rowKey : matrix.keySet()) {
            for (Integer columnKey : matrix.get(rowKey).keySet()) {
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
                NumCoordinate coordinate = new NumCoordinate(rowKey, columnKey);
                coordinates.add(coordinate);
                //System.out.println("Row key: " + rowKey + ", Column key: " + columnKey);
            }
        }
        return coordinates;
    }

    public void printCells() {
        NumCoordinate size = getSize();

        if (size == null) {
            throw new RuntimeException("Print spreadsheet: spreadsheet size is null");
        }

        int cellWidth = 15; // Adjust the width as needed

        for (int i = 1; i <= size.getNumRow(); i++) {
            for (int j = 1; j <= size.getNumColum(); j++) {
                NumCoordinate coordinate = new NumCoordinate(i, j);
                Cell cell = getCell(coordinate);

                if (cell != null) {
                    Content content = cell.getContent();
                    String cellValue;
                    if (content instanceof ContentFormula) {
                        cellValue = ((ContentFormula) content).getValue() + " " + ((ContentFormula) content).getWrittenData();
                    } else if (content instanceof ContentText) {
                        cellValue = ((ContentText) content).getValue();
                    } else if (content instanceof ContentNumerical) {
                        cellValue = String.valueOf(((ContentNumerical) content).getValue());
                    } else {
                        cellValue = "";
                    }
                    int padding = 1;
                    String formattedCell = "";
                    padding = cellWidth - cellValue.length();
                    int leftPadding = padding / 2;
                    int rightPadding = padding - leftPadding;
                    formattedCell = String.format("[%-" + Math.max(1,leftPadding) + "s%s%-" + Math.max(1,rightPadding) + "s] ", "", cellValue, "");

                    System.out.print(formattedCell);
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
}
