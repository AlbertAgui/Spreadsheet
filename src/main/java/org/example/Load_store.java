package org.example;

import java.io.*;

public class Load_store {
    static Spreadsheet loadspreadsheet(String s2vFilePath) {
        try {
            // Read the S2V file
            BufferedReader reader = new BufferedReader(new FileReader(s2vFilePath));
            Spreadsheet t_spreadsheet = new Spreadsheet();


            String line;
            Integer n_row = 1;

            while ((line = reader.readLine()) != null) {
                // Split the line by semicolon
                String[] tokens = line.split(";");
                Integer n_column = 1;
                Num_coordinate num_coordinate = new Num_coordinate();

                // Create a new row
                for (int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        float t_value = Float.parseFloat(tokens[i]);
                        num_coordinate.num_column = n_column;
                        num_coordinate.num_row = n_row;
                        t_spreadsheet.set_cell_value(num_coordinate, t_value);
                        //System.out.println("Cell: " + n_column + n_row + ", value set: " + t_value + "\n");
                    } else {
                        //System.out.println("Cell: " + n_column + n_row + "no value!\n");
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


    static void storespreadsheet(String s2vFilePath, Spreadsheet spreadsheet) {
        try {
            // Read the S2V file
            File file = new File(s2vFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Spreadsheet t_spreadsheet = new Spreadsheet();

            String line;

            for (int i = 1; i <= 3; ++i) { //modify
                for (int j = 1; j <= 3; ++j) {
                    if(j != 1){
                        writer.write(";");
                    }
                    Num_coordinate coor = new Num_coordinate(i,j);
                    writer.write(Float.toString(spreadsheet.get_cell_value(coor)));
                }
                if(i != 3) {
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while interpreting the S2V file: " + e.getMessage());
        }
    }
}
