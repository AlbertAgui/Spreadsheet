package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.ReadingSpreadSheetException;
import edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker.SavingSpreadSheetException;
import org.example.TUI.TextualInterface;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws ContentException, CircularDependencyException, ReadingSpreadSheetException, SavingSpreadSheetException {
        //TextualInterface.input_commands(null);
        //Commands.input_commands(null);
        //TEST 1
        Controller.createEmptySpreadsheet();
        Controller.loadSpreadsheet("marker_save_test_ref.s2v");
        TextualInterface.printCells(Controller.getSpreadsheet().cells);
        Controller.storeSpreadsheet("Data/t2wr.txt");
        //Controller.editCell("A1","100");
        /*Controller.editCell("A1","=A3"); //YES
        Controller.editCell("A3","=A2+4+5 *4/(3+6/ 5+3*(4))"); //YES
        Controller.editCell("C3","=A1+(2*5)/(3+A3)"); //YES*/
        //Controller.editCell("A2","= A1 + SUMA(A1;2)");//YES
        //Controller.editCell("A3","= A1 + SUMA(SUMA(A1:A2;A1);2)");//YES
        /*Controller.editCell("A2","= SUMA()");//NO
        Controller.editCell("A2","= SUMA(");//NO
        Controller.editCell("A2","= SUMA");//NO
        Controller.editCell("A2","= SUMA(A1;2)");//YES
        Controller.editCell("A2","= 2;3 + SUMA(A1;2)");//NO
        Controller.editCell("A2","=
        2:3 + SUMA(A1;2)");//NO*/
        //Controller.editCell("A2","= A1 + SUMA( A1; (2 + SUMA(5:3)))"); //NO
        Controller.createEmptySpreadsheet();
        Controller.editCell("A6", "1");
        Controller.editCell("A7", "2");
        Controller.editCell("A8", "3");
        Controller.editCell("A9", "4");
        Controller.editCell("A10", "5");
        Controller.editCell("A11", "6");
        Controller.editCell("A12", "7");
        Controller.editCell("A13", "8");
        Controller.editCell("A14", "9");
        //Controller.editCell("A1", "=A2+A3+A4+A5");
        TextualInterface.printCells(Controller.getSpreadsheet().cells);
        Controller.editCell("A2", "=A6+A7+A8");
        Controller.editCell("A3", "=A9+A10+A11");
        Controller.editCell("A4", "=A12+A13");
        TextualInterface.printCells(Controller.getSpreadsheet().cells);
        Controller.editCell("A3","= A1 + SUMA(SUMA(A1:A2;A1);2)");
        TextualInterface.printCells(Controller.getSpreadsheet().cells);
        Controller.editCell("A5", "=A14+1");
        Controller.editCell("A25", "This is a string");
        //Controller.editCell("A2","=A1+A7+A8");
        /*Controller.editCell("A1", "1");
        Controller.editCell("A2", "2");
        Controller.editCell("A3", "3");
        Controller.editCell("A4", "4");
        Controller.editCell("A5", "5");
        Controller.editCell("A6", "6");
        Controller.editCell("A7", "7");
        Controller.editCell("A8", "8");
        Controller.editCell("A9", "9");
        Controller.editCell("A10", "10");
        Controller.editCell("A11", "11");
        Controller.editCell("A12", "12");
        Controller.editCell("A13", "13");
        Controller.editCell("A14", "14");
        Controller.editCell("A15", "15");
        Controller.editCell("A16", "16");
        Controller.editCell("A17", "17");
        Controller.editCell("A18", "18");
        Controller.editCell("A19", "19");
        Controller.editCell("A20", "20");
        Controller.editCell("A21", "21");
        Controller.editCell("A22", "22");
        Controller.editCell("A23", "23");
        Controller.editCell("A24", "24");
        Controller.editCell("A25", "This is a string");
        Controller.editCell("J1","= (A5*4)/(A2+A2)+SUMA(A1;A2;3;4;5;A6:A12;MIN(A13:A20))");//YES
        TextualInterface.printCells(Controller.getSpreadsheet().cells);
        Controller.editCell("J1","= SUMA(1+2;3)");//YES
        TextualInterface.printCells(Controller.getSpreadsheet().cells);*/
        //Controller.editCell("A2","= SUMA( A1; SUMA((C1:C5); C1:C5; SUMA(); SUMA()))");//YES
        //Controller.editCell("A2","= 1/0"); //SHOULD NOT WORK

        //TEST 1
        //Controller.createEmptySpreadsheet(); //PASS
        /*Controller.editCell("A1", "100"); //PASS
        System.out.println("---------");
        Controller.editCell("A5", "100+1000000000000000"); //PASS
        System.out.println("---------");
        Controller.editCell("A2","=A1"); //FAIL WEIRD DISPLAY
        System.out.println("---------");*/
        //Controller.editCell("A1","=A1"); //PASS
        /*System.out.println("---------");

        Controller.editCell("A2","=A1"); //FAIL WEIRD DISPLAY
        System.out.println("---------");*/

        /*Controller.createEmptySpreadsheet();
        Controller.loadSpreadsheet("Data/t7.txt");
        Controller.editCell("A2","98.654");
        Controller.editCell("A1","=A2");
        Controller.editCell("A2","=A3");
        Controller.editCell("A3","=A4");
        Controller.storeSpreadsheet("Data/t2wr.txt");*/




        //Controller.storeSpreadsheet("Data/t2wr.txt");
        /*Controller.createEmptySpreadsheet();
        Controller.editCell("A1", "100");
        Controller.editCell("C1", "=100+A1");*/

        //Controller.editCell("A1", "hey");
        //ControllerSpreadsheet.updateDependencies();
//        Commands.input_commands(null);
//        Controller.loadSpreadsheet("Data/t1.txt");
        //Controller.createEmptySpreadsheet();
        //TEST1
        /*Controller.loadSpreadsheet("Data/t1.txt");
        System.out.println("---------");
        Controller.editCell("A1","=A3");
        System.out.println("---------");
        Controller.editCell("A2","=A3");
        System.out.println("---------");
        Controller.editCell("A3","=C1");
        System.out.println("---------");
        Controller.editCell("C1","=C2");
        System.out.println("---------");
        Controller.editCell("C2","=C3");
        System.out.println("---------");
        Controller.editCell("C3","=A3"); //error!!, despres no actualitza
        System.out.println("---------");
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
        /*Controller.createEmptySpreadsheet();
        Controller.editCell("A1","3");
        Controller.editCell("A1","=A1");
        Controller.editCell("A1","=A2");
        Controller.editCell("A1","hey");*/
        //TEST2
        /*Controller.createEmptySpreadsheet();
        Controller.editCell("A1","=A3");
        Controller.editCell("A3","69");
        Controller.editCell("A3","=70");
        Controller.editCell("A3","73");
        Controller.editCell("A1","NOUP");
        Controller.editCell("A1","=A3");*/
        //TEST3
        /*Controller.loadSpreadsheet("Data/t1.txt");
        Controller.editCell("A1","=A2");
        Controller.editCell("A2","=A3");
        Controller.editCell("A3","=69");
        Controller.editCell("A3","=70");
        Controller.editCell("B1","=A3");*/
        //TEST5
        //Controller.loadSpreadsheet("Data/t1.txt");
        //Controller.editCell("A1","=A2");
        //Controller.createEmptySpreadsheet();
        //Controller.editCell("A3","==");
        //Controller.editCell("A1","=A2");
        //Controller.editCell("A1","=A3+4+5*4/(3+6/5+3*(4))");
        //Controller.editCell("A1","hey");
        //Controller.editCell("A1","=A2");
        //Controller.editCell("C3","=A1+4");
        //TEST6
//        Controller.editCell("A1","=A2+3+ADD(3;4)5");
//        Controller.storeSpreadsheet("Data/t2wr.txt");
        //TEST7
        /*Controller.createEmptySpreadsheet();
        Controller.editCell("A1","3");
        System.out.println("---------");
        Controller.editCell("C3","4");
        System.out.println("---------");
        Controller.editCell("C4","=C3");
        System.out.println("---------");
        Controller.editCell("A3","=C3/C4");
        System.out.println("---------");
        Controller.editCell("A2","=A1+(2*5)/(3+A3)");
        System.out.println("---------");
        Controller.storeSpreadsheet("Data/t2wr.txt");*/
//        TEST5
        /*Controller.createEmptySpreadsheet();
        Controller.editCell("A1","=A2+A3+A4+A5");
        Controller.editCell("A2","=A6+A7+A8");
        Controller.editCell("A6","=A1+5");*/
    }
}