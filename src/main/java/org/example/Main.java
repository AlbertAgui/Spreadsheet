package org.example;

import java.text.Normalizer;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet = Load_store.loadspreadsheet("Data/t1.txt");
        String s_coordinate = "A1";
        if (Translate_coordinate.is_correct_coordinate(s_coordinate)) {
            Num_coordinate num_coordinate;
            num_coordinate = Translate_coordinate.translate_coordinate_to_int(s_coordinate);
            num_coordinate.display_coordinate();

            //compute formula value
            Formula.setFormula_body("4+5*4/(3+6/5+3*(4*5))");
            Formula.tokenize();
            if (Formula.is_parseable()) {
                System.out.println("Correct!");
                Formula.generate_postfix();
                float value = Formula.evaluate_postfix();
                spreadsheet.set_cell_value(num_coordinate, value);

                Load_store.storespreadsheet("Data/t2wr.txt", spreadsheet);
            } else {
                System.out.println("Incorrect!");
            }
        } else {
            System.out.println("No valid coordinate\n");
        }
    }
}