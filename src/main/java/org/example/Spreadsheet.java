package org.example;

import java.util.Map;

public class Spreadsheet {
    private Cells cells;
    private NumCoordinate size; //SHOULD BE CHANGED

    public Spreadsheet() {
        this.cells = new Cells();
    }

    public void setSize(NumCoordinate size) {
        this.size = size;
    }

    public NumCoordinate getSize() {
        return this.size;
    }

    public Boolean containsCell(NumCoordinate n_coordinate) {
        return cells.containsCell(n_coordinate);
    }

    public void setCellWrittenData(NumCoordinate n_coordinate, String writtenData) {
        Cell cell;
        if(cells.containsCell(n_coordinate)) {
            cell = cells.getCell(n_coordinate);
        }
        else {
            cell = new Cell();
        }
        cell.setWrittenData(writtenData);
        cells.addCell(n_coordinate, cell);
    }

    public String getCellWrittenData(NumCoordinate n_coordinate) {
        return cells.getCell(n_coordinate).getWrittenData();
    }


    public void setCellValue(NumCoordinate n_coordinate, float value) {
        Cell cell;
        if(cells.containsCell(n_coordinate)) {
            cell = cells.getCell(n_coordinate);
        }
        else {
            cell = new Cell();
        }
        cell.setValue(value);
        //System.out.println("spreadsheet: colum: " + n_coordinate.getNumColum() + " row: " + n_coordinate.getNumRow() + " value: " + cell.getValue() + "\n");
        cells.addCell(n_coordinate, cell);
    }

    public float getCellValue(NumCoordinate n_coordinate) {
        return  cells.getCell(n_coordinate).getValue();
    }

    public void displayCells() {
        this.cells.displayCells();
    }
}
