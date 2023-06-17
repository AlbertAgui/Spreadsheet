package org.example;

import java.io.*;

public class Load_store {
    static Spreadsheet loadspreadsheet(String s2vFilePath) {
        BufferedReader reader = null;
        try {
            // Read the S2V file
            reader = new BufferedReader(new FileReader(s2vFilePath));
            Spreadsheet spreadsheet = new Spreadsheet();

            String line;
            Integer nRow = 1;

            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] tokens = line.split(";");
                Integer nColum = 1;
                NumCoordinate numCoordinate = new NumCoordinate();

                // Create a new row
                for (int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        float t_value = Float.parseFloat(tokens[i]);
                        numCoordinate.setNumColum(nColum);
                        numCoordinate.setNumRow(nRow);
                        //System.out.println("load: colum: " + nColum + " row: " + nRow + ", value set: " + t_value + "\n");
                        Cell cell = new Cell();
                        Content content = new ContentNumerical(); //SHOULD BE CHANGED
                        content.setValue(t_value);
                        cell.setContent(content);
                        spreadsheet.cells.addCell(numCoordinate, cell);
                    }
                    nColum++;
                }
                nRow++;
            }
            return spreadsheet;
        } catch (IOException e) {
            System.out.println("Error loading spreadsheet, path: \"" + s2vFilePath + "\", error message: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing spreadsheet reader" + ", error message: " + e.getMessage());
            }
        }
        return null;
    }


    static void storespreadsheet(String s2vFilePath, Spreadsheet spreadsheet) {
        if(spreadsheet == null) {
            System.out.println("Error storing spreadsheet, invalid spreadsheet");
            return;
        }

        BufferedWriter writer = null;
        try {
            // Read the S2V file
            File file = new File(s2vFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(file));
            String line;

            NumCoordinate size = spreadsheet.cells.getSize();
            Integer nRow = size.getNumRow();
            Integer nCol = size.getNumColum();
            //System.out.println("Size: " + "colum: " +  nCol + ", row: " + nRow + "\n");

            for (int i = 1; i <= nRow; ++i) { //modify
                for (int j = 1; j <= nCol; ++j) {
                    if(j != 1){
                        writer.write(";");
                    }
                    //System.out.println("cnt: Cell: " + j + i + "\n");
                    NumCoordinate coordinate = new NumCoordinate(i,j);

                    Cell cell = spreadsheet.cells.getCell(coordinate);
                    Content content = cell.getContent();

                    if (content instanceof ContentFormula) {
                        writer.write(((ContentFormula) content).getWrittenData());
                    } else if (content instanceof ContentText) {
                        writer.write(((ContentText) content).getValue());
                    } else if (content instanceof ContentNumerical) {
                        writer.write(Float.toString(((ContentNumerical) content).getValue()));
                    }
                }

//                    Object value = spreadsheet.cells.getCell(coordinate).getContent().getValue(); //MODIFY!!
//                    if (value instanceof String) {
//                        writer.write((String) value);
//                    } else if (value instanceof Float) {
//                        writer.write(Float.toString((Float) value));
//                    } //MODIFY!! add something else??
//                }
                if(i != nRow) {
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error storing spreadsheet, path: \"" + s2vFilePath + "\", error message: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing spreadsheet writer" + ", error message: " + e.getMessage());
            }
        }
    }
}
