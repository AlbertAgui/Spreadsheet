package org.example.LoadAndSave;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import org.example.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class LoadFromFile {
    public static Spreadsheet loadSpreadsheet(String filePath) throws CircularDependencyException, ContentException {
        BufferedReader reader = null;
        try {
            // Set the file
            reader = new BufferedReader(new FileReader(filePath));
            Spreadsheet spreadsheet = new Spreadsheet();

            String line;
            Integer nRow = 1;
            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] tokens = line.split(";");
                Integer nColum = 1;

                // Create a new row
                for (int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        //System.out.println("load: colum: " + nColum + " row: " + nRow + ", token: " + tokens[i]);
                        String input = tokens[i];
                        String inputType = ContentTools.getContentType(input);
                        NumCoordinate numCoordinate = new NumCoordinate(nRow, nColum);
                        switch (inputType) {
                            case "Formula":
                                String formulaBody = input.substring(1);
                                float newValue = Formula.compute(formulaBody, spreadsheet);
                                LinkedList<String> new_dependencies = ContentTools.tokenize(formulaBody);
                                ContentTools.updateDependencies(spreadsheet, numCoordinate, new LinkedList<>(), new_dependencies);
                                ControllerSpreadsheet.updateFormula(spreadsheet, numCoordinate, input, newValue); //CHANGE VALUE
                                break;
                            case "Text":
                                ControllerSpreadsheet.updateText(spreadsheet, numCoordinate, input);
                                break;
                            case "Numerical":
                                String inputTrim = input.trim(); //ERASE SPACES
                                float t_value = Float.parseFloat(inputTrim);
                                ControllerSpreadsheet.updateNumerical(spreadsheet, numCoordinate, t_value);
                                break;
                            default:
                                System.out.println("Load: No supported input" + inputType);
                                return null;
                        }
                    }
                    nColum++;
                }
                nRow++;
            }

            if (ContentTools.hasSpreadsheetCircularDependencies(spreadsheet)) {
                throw new CircularDependencyException("Circular dependency");
            }
            ControllerSpreadsheet.recomputeSpreadsheet(spreadsheet); //NEED ERROR CONTROLL?
            //add compute cell values all spreadsheet
            return spreadsheet;
        } catch (CircularDependencyException e) {
            throw new CircularDependencyException("Error loading spreadsheet: " + e.getMessage());
        } catch (ContentException e) {
            throw new ContentException("Error loading spreadsheet: " + e.getMessage());
        } catch (BadCoordinateException e) {
            throw new ContentException("Error loading spreadsheet: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error loading spreadsheet: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error loading spreadsheet: " + e.getMessage());
            }
        }
    }
}
