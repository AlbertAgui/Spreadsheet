package org.example;

import java.util.Map;
import java.util.Set;

public class Spreadsheet {
    public Cells cells;
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

    public void printSpreadsheet() {
        for (int i = 1; i <= size.getNumRow(); i++) {
            for (int j = 1; j <= size.getNumColum(); j++) {
                Cell cell = cells.getCell(new NumCoordinate(i, j));
                if (cell != null) {
                    System.out.print("[ "+ cell.getContent().getValue()+ " ]");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
}