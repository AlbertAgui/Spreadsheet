/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.upc.etsetb.arqsoft.spreadsheet.usecases.marker;

/**
 *
 * @author Juan Carlos Cruellas at Universidad Politécnica de Cataluña
 * An interface that you MUST fill for ENCAPSULATING YOUR OWN PROCESS FOR 
 * creating the whole framework of objects that your code needs, and create your 
 * initial spreadsheet, your own controller and the class that implements the 
 * ISpreadsheetControllerForChecker interface, so that once finalized the execution 
 * of the static method createSpreadsheetController(), everything is ready for 
 * invoking the methods of the object implementing the ISpreadsheetControllerForChecker interface 
 * and starting setting contents in the spreadsheet cells and start checking results 
 * and marking your code
 */
public interface ISpreadsheetFactoryForChecker {
    
    public static ISpreadsheetControllerForChecker createSpreadsheetController() {
        throw new UnsupportedOperationException("ISpreadsheetFactoryForChecker::"
                + "createSpreadsheetController(). You MUST implement the code of "
                + "this method in this class. The code MUST create a class that "
                + "implements the Java interface ISpreadsheetControllerForChecker. "
                + "This code must create the framework that your program requires ("
                + "your spreadsheet controller, your spreadsheet and the objects required "
                + "for editing the contents of the cells, and evaluate formulas and functions)"
              );
    }
}
