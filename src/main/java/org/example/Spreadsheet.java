package org.example;

import java.util.Map;
import java.util.Set;

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

    public Boolean containsCell(NumCoordinate numCoordinate) {
        return cells.containsCell(numCoordinate);
    }

    public void setCellWrittenData(NumCoordinate numCoordinate, String writtenData) {
        Cell cell;
        if(cells.containsCell(numCoordinate)) {
            cell = cells.getCell(numCoordinate);
        }
        else {
            cell = new Cell();
        }
        cell.setWrittenData(writtenData);
        cells.addCell(numCoordinate, cell);
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

    public float getCellValue(NumCoordinate numCoordinate) {
        return  cells.getCell(numCoordinate).getValue();
    }

    public void displayCells() {
        this.cells.displayCells();
    }

    public void addCellDependant(NumCoordinate numCoordinate, NumCoordinate dependant) {
        Cell cell;
        if(cells.containsCell(numCoordinate)) {
            cell = cells.getCell(numCoordinate);
            cell.addDependant(dependant);
            cells.addCell(numCoordinate, cell);
        }
        else {
            System.out.println("Error trying to add cell dependant, non valid cell");
        }
    }

    public void eraseCellDependant(NumCoordinate numCoordinate, NumCoordinate dependant) {
        Cell cell;
        if(cells.containsCell(numCoordinate)) {
            cell = cells.getCell(numCoordinate);
            cell.eraseDependant(dependant);
            cells.addCell(numCoordinate, cell);
        }
        else {
            System.out.println("Error trying to erase cell dependant, non valid cell");
        }
    }

    public Set<NumCoordinate> getDependants(NumCoordinate numCoordinate) {
        if(cells.containsCell(numCoordinate)) {
            return cells.getCell(numCoordinate).getDependants();
        }
        else {
            System.out.println("Error trying to get cell dependants, non valid cell");
            return null;
        }
    }
}
