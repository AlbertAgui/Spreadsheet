package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;

import java.io.*;
import java.util.LinkedList;

public class Load_store { // catch (Exception e)
    static Spreadsheet loadspreadsheet(String FilePath) {
        BufferedReader reader = null;
        try {
            // Set the file
            reader = new BufferedReader(new FileReader(FilePath));
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
                        String inputType = ControllerSpreadsheet.getContentType(input);
                        NumCoordinate numCoordinate = new NumCoordinate(nRow, nColum);
                        switch (inputType) {
                            case "Formula":
                                String formulaBody = input.substring(1);
                                float newValue = Formula.compute(formulaBody, spreadsheet);
                                LinkedList<String> new_dependencies = ControllerSpreadsheet.tokenize(formulaBody);
                                ControllerSpreadsheet.updateDependencies(spreadsheet, numCoordinate, new LinkedList<>(), new_dependencies);
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
                                System.out.println("Load: No suported input" + inputType);
                                return null;
                        }
                    }
                    nColum++;
                }
                nRow++;
            }

            if (ControllerSpreadsheet.hasSpreadsheetCircularDependencies(spreadsheet)) {
                throw new CircularDependencyException("Circular dependency");
            }
            ControllerSpreadsheet.recomputeSpreadsheet(spreadsheet); //NEED ERROR CONTROLL?
            //add compute cell values all spreadsheet
            return spreadsheet;
        } catch (Exception e) {
            throw new RuntimeException("Error loading spreadsheet: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error loading spreadsheet: " + e.getMessage());
            }
        }
    }



    static void storespreadsheet(String FilePath, Spreadsheet spreadsheet) {
        BufferedWriter writer = null;
        try {
            if(spreadsheet == null) {
                throw new RuntimeException("Invalid spreadsheet");
            }

            // Set the file
            File file = new File(FilePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(file));

            NumCoordinate size = spreadsheet.cells.getSize();
            Integer nRow = size.getNumRow();
            Integer nCol = size.getNumColum();
            //System.out.println("Size: " + "colum: " +  nCol + ", row: " + nRow + "\n");

            for (int i = 1; i <= nRow; ++i) { //modify
                for (int j = 1; j <= nCol; ++j) {
                    //Do not semicolon add start of line
                    if(j != 1){
                        writer.write(";");
                    }

                    Cell cell = ControllerSpreadsheet.getCellNull(spreadsheet, new NumCoordinate(i,j));
                    if(cell != null) {
                        Content content = cell.getContent();
                        //Diferent kind of cells, add inheritance!
                        if (content instanceof ContentFormula) {
                            writer.write(((ContentFormula) content).getWrittenData());
                        } else if (content instanceof ContentText) {
                            writer.write(((ContentText) content).getValue());
                        } else if (content instanceof ContentNumerical) {
                            double value = ((ContentNumerical) content).getValue();
                            if (value % 1 == 0) {  // Check if the value is an integer
                                writer.write(Integer.toString((int) value));  // Write integer value without decimal point
                            } else {
                                writer.write(Double.toString(value));  // Write decimal value with decimal point
                            }
//                            writer.write(Float.toString(((ContentNumerical) content).getValue()));
                        }
                    }
                }
                //Do not add newline at last line
                if(i != nRow) {
                    writer.newLine();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error storing spreadsheet: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error storing spreadsheet: " + e.getMessage());
            }
        }
    }
}
