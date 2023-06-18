package org.example;

import java.util.Map;
import java.util.Set;

public class Spreadsheet {
    public Cells cells;

    public Spreadsheet() {
        this.cells = new Cells();
    }

    public void printSpreadsheet() {
        NumCoordinate size = cells.getSize();

        if (size == null) {
            System.out.println("Spreadsheet size is null.");
            return;
        }

        for (int i = 1; i <= size.getNumRow(); i++) {
            for (int j = 1; j <= size.getNumColum(); j++) {
                Cell cell = cells.getCell(new NumCoordinate(i, j));
                if (cell != null) {
                    Content content = cell.getContent();
                    if (content instanceof ContentFormula) {
                        System.out.print("[ " + ((ContentFormula) content).getValue() + " " +  ((ContentFormula) content).getWrittenData() + " ]");
                    } else if (content instanceof ContentText) {
                        System.out.print("[ " + ((ContentText) content).getValue() + " ]");
                    } else if (content instanceof ContentNumerical) {
                        System.out.print("[ " + ((ContentNumerical) content).getValue() + " ]");
                    }
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
}