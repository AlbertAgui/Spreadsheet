package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.NoNumberException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ISpreadsheetControllerForChecker;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ReadingSpreadSheetException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.SavingSpreadSheetException;
import org.example.LoadAndSave.LoadFromFile;
import org.example.LoadAndSave.SaveToFile;


public class Controller implements ISpreadsheetControllerForChecker {
    private static Spreadsheet spreadsheet;

    public static void editCell(String cellId, String input) throws CircularDependencyException, ContentException { //WORKING
        try {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(cellId);
            ControllerSpreadsheet.editCell(spreadsheet, numCoordinate, input);
//            spreadsheet.cells.printCells();
        } catch (CircularDependencyException e) {
            throw new CircularDependencyException("Error loading spreadsheet: " + e.getMessage());
        }
    }

    public static void createEmptySpreadsheet() {
        spreadsheet = new Spreadsheet();
    }

    public static void loadSpreadsheet(String path) throws ReadingSpreadSheetException {
        try {
            spreadsheet = LoadFromFile.loadSpreadsheet(path);
//            TextualInterface.printCells(spreadsheet.cells);
//            spreadsheet.cells.printCells();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ReadingSpreadSheetException();
        }
    }

    public static void storeSpreadsheet(String path) throws SavingSpreadSheetException {
        try {
            SaveToFile.storeSpreadsheet(path, spreadsheet);
//            spreadsheet.cells.printCells();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SavingSpreadSheetException();
        }
    }

    @Override
    public void setCellContent(String cellCoord, String strContent) throws ContentException, BadCoordinateException, CircularDependencyException {
        Controller.editCell(cellCoord, strContent);
    }

    @Override
    public double getCellContentAsDouble(String coord) throws BadCoordinateException, NoNumberException {
       NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(coord);
       Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate);
       Content content = cell.getContent();
        double value = (double) 0;
        if (content instanceof ContentText) {
            throw new NoNumberException();
        }
        else if (content instanceof ContentFormula || content instanceof ContentNumerical) {
            value = (double) content.getValue();
        }
        return value;
//                            writer.write(Float.toString(((ContentNumerical) content).getValue()));
    }

    @Override
    public String getCellContentAsString(String cooord) throws BadCoordinateException {
        NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(cooord);
        return ((ContentText)spreadsheet.cells.getCell(numCoordinate).getContent()).getValue();
    }

    @Override
    public String getCellFormulaExpression(String coord) throws BadCoordinateException {
        NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(coord);
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

    public static Spreadsheet getSpreadsheet(){
        return spreadsheet;
    }
}
