package org.example.LoadAndSave;

import org.example.*;
import org.example.ContentPackage.Content;
import org.example.ContentPackage.ContentFormula;
import org.example.ContentPackage.ContentNumerical;
import org.example.ContentPackage.ContentText;
import org.example.Spreadsheet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveToFile {
    public static void storeSpreadsheet(String filePath, Spreadsheet spreadsheet) {
        BufferedWriter writer = null;
        try {
            if (spreadsheet == null) {
                throw new RuntimeException("Invalid spreadsheet");
            }

            // Set the file
            File file = new File(filePath);
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
                    if (j != 1) {
                        writer.write(";");
                    }

                    Cell cell = SpreadsheetManager.getCellNull(spreadsheet, new NumCoordinate(i, j));
                    if (cell != null) {
                        Content content = cell.getContent();
                        //Different kinds of cells, add inheritance!
                        String contentText = content.getContentText();
                        writer.write(contentText);
                    }
                }
                //Do not add a newline at the last line
                if (i != nRow) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error storing spreadsheet: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error storing spreadsheet: " + e.getMessage());
            }
        }
    }
}
