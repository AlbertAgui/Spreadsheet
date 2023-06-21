package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.CircularDependencyException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula { //1 + 2-4 //The preference in order used to find could be parametrized
    //Tokenizer

    //Generate postfix


    public static Float compute(String formula_body, Spreadsheet spreadsheet) throws ContentException,BadCoordinateException {
        float value = 0;
        try {
            LinkedList<String> tokens = Tokenize.tokenize(formula_body);
            if (!Parsing.is_parseable(tokens)) {
                throw new ContentException("No parseable tokens");
            }
            LinkedList<String> postfix = GeneratePostfix.generate_postfix(tokens);
            value = EvaluatePostfix.evaluate_postfix(spreadsheet, postfix, tokens);
        } catch (Exception e) {
            if (e instanceof BadCoordinateException) {
                throw new BadCoordinateException("Compute: " + e.getMessage());
            }
            if (e instanceof ContentException) {
                throw new ContentException("Compute: " + e.getMessage());
            } else {
                throw new RuntimeException("Compute: " + e.getMessage());
            }
        }
        return value;
    }


}