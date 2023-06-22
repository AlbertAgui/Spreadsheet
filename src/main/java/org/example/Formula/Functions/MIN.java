package org.example.Formula.Functions;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import org.example.*;
import org.example.ContentPackage.Content;
import org.example.ContentPackage.ContentFormula;
import org.example.ContentPackage.ContentNumerical;
import org.example.ContentPackage.ContentText;
import org.example.Formula.Parsing;

import java.util.Stack;

public class MIN {
    public static float minFunction(Stack<String> aux_stack, Spreadsheet spreadsheet, int numArgs) throws ContentException {
        float minValue = 0;
        float localValue = 0;
        String next = aux_stack.pop();
        if (Parsing.is_cell_id(next)) {
            NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(next);
            Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate);
            Content content = cell.getContent();

            if (content instanceof ContentFormula) {
                localValue = ((ContentFormula) content).getValue();
            } else if (content instanceof ContentText) {
                throw new ContentException("Cell content text is a formula cell dependency!");
            } else if (content instanceof ContentNumerical) {
                localValue = ((ContentNumerical) content).getValue();
            }
            minValue = localValue;
        } else {
            minValue = Float.parseFloat(next);
        }

        for (int i = 1; i < numArgs; ++i) {
            next = aux_stack.pop();
            if (Parsing.is_cell_id(next)) {
                NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(next);
                Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate);
                Content content = cell.getContent();

                if (content instanceof ContentFormula) {
                    localValue = ((ContentFormula) content).getValue();
                } else if (content instanceof ContentText) {
                    throw new ContentException("Cell content text is a formula cell dependency!");
                } else if (content instanceof ContentNumerical) {
                    localValue = ((ContentNumerical) content).getValue();
                }

                if (localValue < minValue)
                    minValue = localValue;
            } else {
                localValue = Float.parseFloat(next);
                if (localValue < minValue)
                    minValue = localValue;
            }
        }

        return minValue;
    }
}
