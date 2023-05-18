package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Load_store {
    static Spreadsheet loadspreadsheet(String s2vFilePath) {
        try {
            // Read the S2V file
            BufferedReader reader = new BufferedReader(new FileReader(s2vFilePath));
            Spreadsheet t_spreadsheet = new Spreadsheet();

            String line;
            char n_row = '1';

            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] tokens = line.split(";");
                char n_column = 'A';

                // Create a new row
                for (int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        float t_value = Float.parseFloat(tokens[i]);
                        t_spreadsheet.set_cell_value(Character.toString(n_column) + Character.toString(n_row), t_value);
                        System.out.println("Cell: " + n_column + n_row + ", value set: " + t_value + "\n");
                    } else {
                        System.out.println("Cell: " + n_column + n_row + "no value!\n");
                    }
                    n_column++;
                }
                n_row++;
            }
            return t_spreadsheet;
        } catch (IOException e) {
            System.out.println("An error occurred while interpreting the S2V file: " + e.getMessage());
        }
        return null;
    }

}
