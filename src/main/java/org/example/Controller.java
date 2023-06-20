package org.example;

import java.io.IOException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.NoNumberException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ISpreadsheetControllerForChecker;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ReadingSpreadSheetException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.SavingSpreadSheetException;
import org.example.Content;


public class Controller implements ISpreadsheetControllerForChecker {
    private static Spreadsheet spreadsheet;

    public static void editCell(String cellId, String input) { //WORKING
        try {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(cellId);
            ControllerSpreadsheet.editCell(spreadsheet, numCoordinate, input);
            spreadsheet.cells.printCells();
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
            spreadsheet.cells.printCells();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void storeSpreadsheet(String path) {
        try {
            Load_store.storespreadsheet(path, spreadsheet);
            spreadsheet.cells.printCells();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCellContent(String cellCoord, String strContent) throws ContentException, BadCoordinateException, CircularDependencyException {
        Controller.editCell(cellCoord, strContent);
    }

    @Override
    public double getCellContentAsDouble(String coord) throws BadCoordinateException, NoNumberException {
        NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(coord);
        ContentNumerical content = (ContentNumerical)spreadsheet.cells.getCell(numCoordinate).getContent();
        if(content == null)
            return (double) 0;
        float value = content.getValue();
        double value2 = (double) value;
        return value2;
    }

    @Override
    public String getCellContentAsString(String cooord) throws BadCoordinateException {
        NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(cooord);
        return ((ContentText)spreadsheet.cells.getCell(numCoordinate).getContent()).getValue();
    }

    @Override
    public String getCellFormulaExpression(String coord) throws BadCoordinateException {
        NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(coord);
        return ((ContentFormula)spreadsheet.cells.getCell(numCoordinate).getContent()).getWrittenData();
//        ContentFormula content = spreadsheet.cells.getCell(numCoordinate).getContent();
//        ((ContentFormula) content).getWrittenData();
    }

    @Override
    public void saveSpreadSheetToFile(String nameInUserDir) throws SavingSpreadSheetException {
        this.storeSpreadsheet(nameInUserDir);
    }

    @Override
    public void readSpreadSheetFromFile(String nameInUserDir) throws ReadingSpreadSheetException {
        Controller.loadSpreadsheet(nameInUserDir);
    }

    public static ISpreadsheetControllerForChecker createSpreadsheetController() {
        return new Controller();
    }
}
