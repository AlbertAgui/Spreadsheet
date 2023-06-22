package org.example.Formula.Functions;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import org.example.*;
import org.example.ContentPackage.Content;
import org.example.ContentPackage.ContentFormula;
import org.example.ContentPackage.ContentNumerical;
import org.example.ContentPackage.ContentText;
import org.example.Formula.Parsing;

import java.util.Stack;

public class SUMA {
    public static float sumaFunction(Stack<String> aux_stack, Spreadsheet spreadsheet, int numArgs) throws ContentException {
        float result = 0;
        for (int i = 0; i < numArgs; ++i) {
            String next = aux_stack.pop();
            if (Parsing.is_cell_id(next)) {
                NumCoordinate numCoordinate = Translate_coordinate.translateCellIdToCoordinateTo(next);
                Cell cell = SpreadsheetManager.getCellAny(spreadsheet, numCoordinate);
                Content content = cell.getContent();
                float value = 0;

                if (content instanceof ContentFormula) {
                    value = ((ContentFormula) content).getValue();
                } else if (content instanceof ContentText) {
                    throw new ContentException("Cell content text is a formula cell dependency!");
                } else if (content instanceof ContentNumerical) {
                    value = ((ContentNumerical) content).getValue();
                }

                result += value;
            } else {
                result += Float.parseFloat(next);
            }
        }

        return result;
    }
}
