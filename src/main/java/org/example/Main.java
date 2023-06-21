package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        //Commands.input_commands(null);
        //TEST 1
        Controller.createEmptySpreadsheet();
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
        Controller.editCell("A2","= 2:3 + SUMA(A1;2)");//NO*/
        //Controller.editCell("A2","= A1 + SUMA( A1; (2 + SUMA(5:3)))"); //NO
        Controller.editCell("C3","8");
        Controller.editCell("A2","= A1 + SUMA( A1; SUMA((C1:C5); C1:C5))");//YES
        Controller.editCell("A2","= SUMA( A1; SUMA((C1:C5); C1:C5; SUMA(); SUMA()))");//YES
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
    }
}