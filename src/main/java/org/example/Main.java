package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Canvi, Hello and welcome!\n");
        Spreadsheet spreadsheet;
        spreadsheet = Load_store.loadspreadsheet("Data/t1.txt");



        //spreadsheet.set_cell_value("A1",23);
        spreadsheet.get_cell_value("A1");
    }
}