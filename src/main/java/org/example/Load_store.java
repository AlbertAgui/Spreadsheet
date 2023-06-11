package org.example;

import java.io.*;

public class Load_store {
    static Spreadsheet loadspreadsheet(String s2vFilePath) {
        try {
            // Read the S2V file
            BufferedReader reader = new BufferedReader(new FileReader(s2vFilePath));
            Spreadsheet spreadsheet = new Spreadsheet();

            String line;
            //Integer cntRow = 0, cntColum = 0;
            Integer nRow = 1;

            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] tokens = line.split(";");
                Integer nColum = 1;
                NumCoordinate numCoordinate = new NumCoordinate();
                //if(cntColum == 0) cntColum = tokens.length;

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
                    } else { //SHOULD BE CHANGED
                        /*numCoordinate.setNumColum(nColum);
                        numCoordinate.setNumRow(nRow);
                        //System.out.println("load: colum: " + nColum + " row: " + nRow + " no value!\n");
                        Cell cell = new Cell();
                        Content content = new ContentNumerical(); //SHOULD BE CHANGED
                        content.setValue(0);
                        cell.setContent(content);
                        spreadsheet.cells.addCell(numCoordinate, cell);*/
                    }
                    nColum++;
                }
                nRow++;
            }
            //cntRow = nRow - 1;

            /*NumCoordinate size = new NumCoordinate(cntRow, cntColum);
            //System.out.println("Size: " + "colum: " +  cntColum + ", row: " + cntRow + "\n");
            spreadsheet.setSize(size);*/
            return spreadsheet;
        } catch (IOException e) {
            System.out.println("An error occurred while interpreting the S2V file: " + e.getMessage());
        }
        return null;
    }


    static void storespreadsheet(String s2vFilePath, Spreadsheet spreadsheet) {
        try {
            // Read the S2V file
            File file = new File(s2vFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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

                    Object value = spreadsheet.cells.getCell(coordinate).getContent().getValue(); //MODIFY!!
                    if (value instanceof String) {
                        writer.write((String) value);
                    } else if (value instanceof Float) {
                        writer.write(Float.toString((Float) value));
                    } /*else {
                        //writer.write("");
                        System.out.println("No data, row: " + i + " col: " + j);
                    }*/
                }
                if(i != nRow) {
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while interpreting the S2V file: " + e.getMessage());
        }
    }
}
