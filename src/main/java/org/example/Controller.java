package org.example;

import java.io.IOException;

public class Controller {
    private static Spreadsheet spreadsheet;

    public static void editCell(String cellId, String input) { //WORKING
        try {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(cellId);
            ControllerSpreadsheet.editCell(spreadsheet, numCoordinate, input);
            spreadsheet.printSpreadsheet();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createEmptySpreadsheet() {
        spreadsheet = new Spreadsheet();
    }

    public static void loadSpreadsheet(String path) {
        try {
            spreadsheet = Load_store.loadspreadsheet(path);
            spreadsheet.printSpreadsheet();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void storeSpreadsheet(String path) {
        try {
            Load_store.storespreadsheet(path, spreadsheet);
            spreadsheet.printSpreadsheet();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
