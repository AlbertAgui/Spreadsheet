package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Canvi, Hello and welcome!\n");
        Spreadsheet spreadsheet = new Spreadsheet();
        String s_coordinate = "AA23";
        Num_coordinate num_coordinate;

        spreadsheet = Load_store.loadspreadsheet("Data/t1.txt");

        //spreadsheet.get_cell_value(num_coordinate);

        if(Translate_coordinate.is_correct_coordinate(s_coordinate)) {
            num_coordinate = Translate_coordinate.translate_coordinate_to_int(s_coordinate);

            System.out.println("col, row:" + num_coordinate.num_column + " , " + num_coordinate.num_row + "\n");

            spreadsheet.set_cell_value(num_coordinate, 54);

            //spreadsheet.set_cell_value("A1",23);
            spreadsheet.get_cell_value(num_coordinate);
        } else {
            System.out.println("No valid coordinate\n");
        }

        Load_store.storespreadsheet("Data/t2wr.txt", spreadsheet);
    }
}