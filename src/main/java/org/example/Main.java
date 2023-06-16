package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Commands.input_commands(null);
        Controller.loadSpreadsheet("Data/t1.txt");
        Controller.editCell("B2","=A1+100");
        Controller.editCell("C3","=B2+1000");
        Controller.editCell("A3","=C3+10000");
        //Controller.editCell("A1","=A3+4+5*4/(3+6/5+3*(4))");
        //Controller.editCell("A1","3");
        Controller.storeSpreadsheet("Data/t2wr.txt");
    }
}