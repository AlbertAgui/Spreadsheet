package org.example;

import org.example.Cells;
import org.example.NumCoordinate;

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
}