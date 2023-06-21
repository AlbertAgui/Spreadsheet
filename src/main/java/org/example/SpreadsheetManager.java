package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import org.example.ContentPackage.*;
import org.example.Formula.Formula;

import java.util.*;

public class SpreadsheetManager {

    public static void updateFormula(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String writtenContent, float value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentFormula)) {
            new_content = new ContentFormula();
        }
        ((ContentFormula) new_content).setWrittenData(writtenContent);
        ((ContentFormula) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }

    public static void updateText(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentText)) {
            new_content = new ContentText();
        }
        ((ContentText) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }

    public static void updateNumerical(Spreadsheet spreadsheet, NumCoordinate numCoordinate, float value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentNumerical)) {
            new_content = new ContentNumerical();
        }
        ((ContentNumerical) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }


    private static void recomputeCell(Spreadsheet spreadsheet, NumCoordinate numCoordinate, Queue<NumCoordinate> stack, Set<NumCoordinate> visited) throws ContentException {
        visited.add(numCoordinate);
        Cell cell = SpreadsheetManager.getCellExisting(spreadsheet,numCoordinate);
        Content content = cell.getContent();
        if(content instanceof ContentFormula) {
            String body = ((ContentFormula) content).getWrittenData();
            LinkedList <String> dependencies = ContentTools.getDependencies(body);
            if(dependencies.isEmpty()) {
                stack.add(numCoordinate);
            } else {
                for (String dependency : dependencies) {
                    NumCoordinate coordinate;
                    coordinate = Translate_coordinate.translateCellIdToCoordinateTo(dependency);
                    if(!visited.contains(coordinate)) {
                        recomputeCell(spreadsheet, coordinate, stack, visited);
                    }
                }
                stack.add(numCoordinate);
            }
        } /*else if (content instanceof ContentText) {
            throw new Exception("load computing spreadsheet text dependency, this shouldn't happen");
        }*/
    }


    public static void recomputeSpreadsheet(Spreadsheet spreadsheet) throws ContentException {
        Set<NumCoordinate> visited = new HashSet<>();
        for (NumCoordinate coordinate: spreadsheet.cells.getCoordinateSet()) {
            if(!visited.contains(coordinate)) {
                Queue<NumCoordinate> queue = new LinkedList<>();
                recomputeCell(spreadsheet, coordinate, queue, visited);
                while(!queue.isEmpty()) {
                    NumCoordinate numCoordinate = queue.remove();
                    Cell cell = SpreadsheetManager.getCellExisting(spreadsheet, numCoordinate);
                    String writtenData = ((ContentFormula)cell.getContent()).getWrittenData(); //SHOULD BE FORMULA
                    Float value = Formula.compute(writtenData, spreadsheet);
                    updateFormula(spreadsheet, numCoordinate, writtenData, value);
                }
            }
        }
    }


    public static void editCell(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String input) throws ContentException, CircularDependencyException { //WORKING
        try {
            String inputType = ContentTools.getContentType(input);
            String formulaBody = input.substring(1);
            Cell old_cell = null;
            Content old_content = null;
            LinkedList<String> old_dependencies = null;
            LinkedList<String> new_dependencies = null;
            float newValue = 0;
            switch (inputType) {
                case "Formula":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    newValue = Formula.compute(formulaBody, spreadsheet);
                    new_dependencies = ContentTools.getDependencies(formulaBody);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = ContentTools.getDependencies(old_body);
                        } else {
                            old_dependencies = new LinkedList<>();
                        }
                    } else {
                        old_dependencies = new LinkedList<>();
                    }
                    ContentTools.updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                    updateFormula(spreadsheet, numCoordinate, formulaBody, newValue);
                    if (ContentTools.hasCellCircularDependency(spreadsheet, numCoordinate)) {
                        ContentTools.updateDependencies(spreadsheet, numCoordinate, new_dependencies, old_dependencies); //redo dependencies

                        //UNDO UPDATE FORMULA
                        if (old_content == null) { //NOT BEST SOLUTION, ALL CELL SHOULD BE COPIED RECURSIVELLY, NOT ROBUST
                            spreadsheet.cells.eraseCell(numCoordinate);
                        } else {
                            Cell recoverCell = spreadsheet.cells.getCell(numCoordinate);
                            recoverCell.setContent(old_content);
                            spreadsheet.cells.addCell(numCoordinate, recoverCell);
                        }
                        throw new CircularDependencyException("Circular dependency");
                    } else {
                        //recompute values
                        ContentTools.recomputeCellDependants(spreadsheet, numCoordinate);
                    }

                    break;
                case "Text":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = ContentTools.getDependencies(old_body);
                            new_dependencies = new LinkedList<>();
                            ContentTools.updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                        }
                    }
                    //update written, value
                    updateText(spreadsheet, numCoordinate, input);
                    //recompute values
                    ContentTools.recomputeCellDependants(spreadsheet, numCoordinate);
                    break;
                case "Numerical":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = ContentTools.getDependencies(old_body);
                            new_dependencies = new LinkedList<>();
                            ContentTools.updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                        }
                    }
                    //update written, value
                    String inputTrim = input.trim(); //ERASE SPACES
                    newValue = Float.parseFloat(inputTrim);
                    updateNumerical(spreadsheet, numCoordinate, newValue);
                    //recompute values
                    ContentTools.recomputeCellDependants(spreadsheet, numCoordinate);
                    break;
                default:
                    System.out.println("No concrete content factory method for " + inputType);
            }
        } catch (BadCoordinateException e) {
                throw new BadCoordinateException("Error Edit Cell: " + e.getMessage());
        } catch (ContentException e) {
            throw new ContentException("Error Edit Cell: " + e.getMessage());
        } catch (CircularDependencyException e) {
            throw new CircularDependencyException("Error Edit Cell: " + e.getMessage());
        } catch (Exception e) {
            throw new ContentException("Error Edit Cell: " + e.getMessage());
        }
    }


    //UTILS
    //GET CELL
    public static Cell getCellExisting (Spreadsheet spreadsheet, NumCoordinate numCoordinate) throws ContentException {
        Cell cell = spreadsheet.cells.getCell(numCoordinate);
        if (cell == null)
            throw new ContentException("Cell not found: colum: " + numCoordinate.getNumColum() + ", row: " + numCoordinate.getNumRow());
        return cell;
    }

    public static Cell getCellNull (Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        return spreadsheet.cells.getCell(numCoordinate);
    }

    public static Cell getCellAny (Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        Cell cell = spreadsheet.cells.getCell(numCoordinate);
        if (cell == null)
            cell = new Cell();
        return cell;
    }

    //ADD CELL

}