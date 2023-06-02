package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Spreadsheet spreadsheet = Load_store.loadspreadsheet("Data/t1.txt");
        String s_coordinate = "A1";
        if (Translate_coordinate.is_correct_coordinate(s_coordinate)) {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(s_coordinate);

            //compute formula value
            float value = Formula.compute("4+5*4/(3+6/5+3*(4*5))", spreadsheet);
            spreadsheet.setCellValue(numCoordinate, value);
            Load_store.storespreadsheet("Data/t2wr.txt", spreadsheet);
        } else {
            System.out.println("No valid coordinate\n");
        }
    }
}