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
        NumCoordinate size = cells.getSize();

        if (size == null) {
            System.out.println("Spreadsheet size is null.");
            return;
        }

        int cellWidth = 10; // Adjust the width as needed

        for (int i = 1; i <= size.getNumRow(); i++) {
            for (int j = 1; j <= size.getNumColum(); j++) {
                NumCoordinate coordinate = new NumCoordinate(i, j);
                Cell cell = cells.getCell(coordinate);

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
                    System.out.printf("[%-" + cellWidth + "s] ", cellValue);
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }

//    Version2
//    public void printSpreadsheet() {
//        NumCoordinate size = cells.getSize();
//
//        if (size == null) {
//            System.out.println("Spreadsheet size is null.");
//            return;
//        }
//
//        for (int i = 1; i <= size.getNumRow(); i++) {
//            for (int j = 1; j <= size.getNumColum(); j++) {
//                NumCoordinate coordinate = new NumCoordinate(i, j);
//                Cell cell = cells.getCell(coordinate);
//
//                if (cell != null) {
//                    Content content = cell.getContent();
//                    if (content instanceof ContentFormula) {
//                        System.out.print("[ " + ((ContentFormula) content).getValue() + " " + ((ContentFormula) content).getWrittenData() + " ]");
//                    } else if (content instanceof ContentText) {
//                        System.out.print("[ " + ((ContentText) content).getValue() + " ]");
//                    } else if (content instanceof ContentNumerical) {
//                        System.out.print("[ " + ((ContentNumerical) content).getValue() + " ]");
//                    }
//                } else {
//                    System.out.print("[ ] ");
//                }
//            }
//            System.out.println();
//        }
//    }
//    public void printSpreadsheet() {
//        NumCoordinate size = cells.getSize();
//
//        if (size == null) {
//            System.out.println("Spreadsheet size is null.");
//            return;
//        }
//
//        for (int i = 1; i <= size.getNumRow(); i++) {
//            for (int j = 1; j <= size.getNumColum(); j++) {
//                NumCoordinate coordinate = new NumCoordinate(i,j);
//                Cell cell = cells.getCell(new NumCoordinate(i, j));
//
//                if (cell != null) {
//                    Content content = cell.getContent();
//                    if (content instanceof ContentFormula) {
//                        System.out.print("[ " + ((ContentFormula) content).getValue() + " " +  ((ContentFormula) content).getWrittenData() + " ]");
//                    } else if (content instanceof ContentText) {
//                        System.out.print("[ " + ((ContentText) content).getValue() + " ]");
//                    } else if (content instanceof ContentNumerical) {
//                        System.out.print("[ " + ((ContentNumerical) content).getValue() + " ]");
//                    }
//                } else {
//                    System.out.print("[ ] ");
//                }
//            }
//            System.out.println();
//        }
//    }
}