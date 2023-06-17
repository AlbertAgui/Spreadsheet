package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Commands.input_commands(null);
        Controller.loadSpreadsheet("Data/t1.txt");
        //Controller.createEmptySpreadsheet();
        //TEST1
        /*Controller.editCell("A1","=A3");
        Controller.editCell("A2","=A3");
        Controller.editCell("A3","=C1");
        Controller.editCell("C1","=C2");
        Controller.editCell("C2","=C3");
        Controller.editCell("C3","=A3"); //error!!, despres no actualitza
        Controller.editCell("C3","=B1");*/
        //TEST1 NICE!!
        /*Controller.editCell("A1","=A3");
        Controller.editCell("A2","=A3");
        Controller.editCell("A3","=C1");
        Controller.editCell("C1","=C2");
        Controller.editCell("C2","=C3");
        Controller.editCell("C3","=B1");
        Controller.editCell("B1","=3");*/
        //TEST
        /*Controller.editCell("A1","=A1");
        //Controller.editCell("A1","=A2");
        Controller.editCell("A1","hey");
        //Controller.editCell("A1","3");*/
        //TEST2
        /*Controller.editCell("A1","=A3");
        Controller.editCell("A3","69");
        Controller.editCell("A3","=70");
        Controller.editCell("A3","73");
        Controller.editCell("A1","NOUP");
        Controller.editCell("A1","=A3");*/
        //TEST3
        /*Controller.editCell("A1","=A2");
        Controller.editCell("A2","=A3");
        Controller.editCell("A3","=69");
        Controller.editCell("A3","=70");
        Controller.editCell("B1","=A3");*/

        //Controller.editCell("A1","=A2");
        //Controller.editCell("A1","=A3+4+5*4/(3+6/5+3*(4))");
        //Controller.editCell("A1","hey");
        //Controller.editCell("A1","=A2");
        //Controller.editCell("C3","=A1+4");
        //TEST4
        Controller.editCell("A1","=A2+3+ADD(3;4)5");
        Controller.storeSpreadsheet("Data/t2wr.txt");
    }
}