package org.example;

public class Controller {
    private static Spreadsheet spreadsheet;

    //private static showSpreadsheet() {}//PENDING

    //public static readComandFromFile() {} //PENDING

    public static void editCell(String coordinate, String input) { //WORKING
        NumCoordinate numCoordinate;
        numCoordinate = Translate_coordinate.translate_coordinate_to_int(coordinate);
        ControllerSpreadsheet.editCell(spreadsheet, numCoordinate, input);
//        spreadsheet.printSpreadsheet();
    }

    public static void createEmptySpreadsheet() {
        spreadsheet = new Spreadsheet(); spreadsheet.printSpreadsheet();
    }

    public static void loadSpreadsheet(String path) {
        spreadsheet = Load_store.loadspreadsheet(path); spreadsheet.printSpreadsheet();
    }

    public static void storeSpreadsheet(String path) {
        Load_store.storespreadsheet(path, spreadsheet); spreadsheet.printSpreadsheet();
    }
}
