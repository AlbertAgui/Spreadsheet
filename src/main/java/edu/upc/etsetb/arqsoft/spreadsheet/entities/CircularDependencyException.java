/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

/**
 *
 * @author Juan Carlos Cruellas at Universidad Politécnica de Cataluña. 
 * This is the exception that the class that implements the ISpreadsheetControllerForChecker 
 * has to throw when during the process of computing the value of a formula your code 
 * detects that the formula includes circular dependencies
 * 
 */
public class CircularDependencyException extends Exception {

    /**
     * Creates a new instance of <code>CircularDependencyException</code>
     * without detail message.
     */
    public CircularDependencyException() {
    }

    /**
     * Constructs an instance of <code>CircularDependencyException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CircularDependencyException(String msg) {
        super(msg);
    }
}
