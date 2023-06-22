package org.example.Formula;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import org.example.*;
import org.example.ContentPackage.Content;
import org.example.ContentPackage.ContentFormula;
import org.example.ContentPackage.ContentNumerical;
import org.example.ContentPackage.ContentText;
import org.example.Formula.Functions.MAX;
import org.example.Formula.Functions.MIN;
import org.example.Formula.Functions.PROMEDIO;
import org.example.Formula.Functions.SUMA;
import org.example.Spreadsheet;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class EvaluatePostfix {
    private static Queue<Integer> getFunctionsNumArgs(LinkedList<String> tokens) {
        Queue<Integer> functionsNumArgs = new LinkedList<>();
        Stack<Integer> pendingArgCnts = new Stack<>();
        int pendingFunctions = 0;
        int localArgCnt = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(Parsing.is_function(next)) {
                if (pendingFunctions > 0) {
                    pendingArgCnts.push(localArgCnt);
                    localArgCnt = 0;
                }
                pendingFunctions++;
            } else if(Parsing.is_closed_claw(next)) {
                if (pendingFunctions > 0) { //functions being computed
                    localArgCnt++; //1 extra token for final argument
                    functionsNumArgs.add(localArgCnt);
                    if (pendingFunctions > 1) { //Pending functions stored
                        localArgCnt = pendingArgCnts.pop();
                    } else {
                        localArgCnt = 0;
                    }
                    pendingFunctions--;
                }
            } else if (Parsing.isColon(next)) { //Compute extra arguments
                String down, top;
                top = tokens.get(i+1);
                down = tokens.get(i-1);
                NumCoordinate endCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(top);
                NumCoordinate startCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(down);
                int startRow, endRow, startColum, endColum;
                startRow = startCoordinate.getNumRow();
                endRow = endCoordinate.getNumRow();
                startColum = startCoordinate.getNumColum();
                endColum = endCoordinate.getNumColum();
                int rowCnt = endRow - startRow + 1;
                int ColumCnt = endColum - startColum + 1;
                localArgCnt = localArgCnt + (rowCnt * ColumCnt) - 1; //ERASE THE EXTRA ONE
            } else if (Parsing.isSemicolon(next)) {
                localArgCnt++;
            }
        }
        return functionsNumArgs;
    }


    //Evaluate postfix
    public static float operationCompute (Spreadsheet spreadsheet, String operator, String l_operand, String r_operand) throws ContentException {
        float l_op = 0, r_op = 0;
        if(Parsing.is_cell_id(l_operand)) { //THIS HAPPENS??
            //throw new RuntimeException("Evaluate postfix: operand: " + l_operand + "should be numerical");
            NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(l_operand);
            Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
            Content content = cell.getContent();
            Float value = (float) 0;
            value = content.getContentNumber();
            l_op = value;
        } else {
            l_op = Float.parseFloat(l_operand);
            //System.out.println("l_op: " + l_op);
        }

        if(Parsing.is_cell_id(r_operand)) {
            //throw new RuntimeException("Evaluate postfix: operand: " + r_operand + "should be numerical");
            NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(r_operand);
            Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
            Content content = cell.getContent();
            Float value = (float) 0;
            value = content.getContentNumber();
            r_op = value;
        } else {
            r_op = Float.parseFloat(r_operand);
            //System.out.println("r_op: " + r_op);
        }

        float result = switch (operator) {
            case "+" -> l_op + r_op;
            case "-" -> l_op - r_op;
            case "*" -> l_op * r_op;
            case "/" -> l_op / r_op;
            default -> throw new ContentException("Evaluate postfix: operator: " + operator + "not supported"); //error should not be needed
        };
        return result;
    }



    public static float functionCompute(Spreadsheet spreadsheet, String function, int numArgs, Stack<String> aux_stack) throws ContentException {
        float result = 0;

        switch (function) {
            case "SUMA":
                result = SUMA.sumaFunction(aux_stack, spreadsheet, numArgs);
                break;
            case "MIN":
                result = MIN.minFunction(aux_stack, spreadsheet, numArgs);
                break;
            case "MAX":
                result = MAX.maxFunction(aux_stack, spreadsheet, numArgs);
                break;
            case "PROMEDIO":
                result = PROMEDIO.promedioFunction(aux_stack, spreadsheet, numArgs);
                break;
            default:
                throw new ContentException("Evaluate postfix: function: " + function + " not supported");
        }

        return result;
    }


    public static float evaluate_postfix(Spreadsheet spreadsheet, LinkedList<String> postfix, LinkedList<String> tokens) throws ContentException, CircularDependencyException { //-1 not suported!
        Stack<String> aux_stack = new Stack<>();
        try {
            Queue<Integer> functionsNumArgs = getFunctionsNumArgs(tokens);
            for (int i = 0; i < postfix.size(); ++i) {
                String next = postfix.get(i);
                if (Parsing.is_number(next) || Parsing.is_cell_id(next)) { //MUST BE MODIFIED
                    aux_stack.push(next);
                } else if (Parsing.is_operator(next)) { //should not be necessary, but in functions something here will be modified
                    String down, top;
                    top = aux_stack.pop();
                    down = aux_stack.pop();
                    aux_stack.push(Float.toString(operationCompute(spreadsheet, next, down, top))); //ojo format!!
                } else if (Parsing.is_function(next)) {
                    aux_stack.push(Float.toString(functionCompute(spreadsheet, next, functionsNumArgs.remove(), aux_stack)));
                } else if (Parsing.isColon(next)) {
                    float result = 0;
                    String down, top;
                    top = aux_stack.pop();
                    down = aux_stack.pop();
                    NumCoordinate endCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(top);
                    NumCoordinate startCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(down);
                    Integer startRow, endRow, startColum, endColum;
                    startRow = startCoordinate.getNumRow();
                    endRow = endCoordinate.getNumRow();
                    startColum = startCoordinate.getNumColum();
                    endColum = endCoordinate.getNumColum();
                    if (startRow > endRow) throw new ContentException("Incorrect rang");
                    if (startColum > endColum) throw new ContentException("Incorrect rang");
                    for (int col = startColum; col <= endColum; ++col) {
                        for (int row = startRow; row <= endRow; ++row) {
                            NumCoordinate localCoordinate = new NumCoordinate(row, col);
                            Cell cell = SpreadsheetManager.getCellAny(spreadsheet, localCoordinate); //To get 0 from empty cells
                            Content content = cell.getContent();
                            Float value = (float) 0;
                            value = content.getContentNumber();
                            aux_stack.push(Float.toString(value));
                        }
                    }
                }
            }
            String next = aux_stack.pop();
            if (Parsing.is_cell_id(next)) {
                NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(next);
                Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
                Content content = cell.getContent();
                Float value = (float) 0;
                value = content.getContentNumber();
                return value;
            } else {
                return Float.parseFloat(next);
            }
        } catch (BadCoordinateException e) {
            throw new BadCoordinateException("Evaluate postfix: " + e.getMessage());
        } catch (ContentException e) {
            throw new ContentException("Evaluate postfix: " + e.getMessage());
        } catch (Exception e) {
            throw new ContentException("Evaluate postfix: " + e.getMessage());
        }
    }
}
