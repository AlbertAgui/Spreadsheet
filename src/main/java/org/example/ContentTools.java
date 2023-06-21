package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentTools {

    public static final List<String> TokenMatchInfos = new ArrayList<>(Arrays.asList( //static="class instance, unique", final="static, constant"
            "\s",//is this needed?
            "[+-]",
            "[*/]",
            "\\(",
            "\\)",//
            "[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?",//093 will be supported... is it fine?
            "([A-Z]+)(\\d+)",
            "\\:",
            "\\;",
            "SUMA",
            "MIN",
            "MAX",
            "PROMEDIO" //function
    ));
    private static String textPattern = "^(?!=).*$";
    private static String formulaPattern = "^=.*$";
    private static String numPattern = "^"+"\\s*"+TokenMatchInfos.get(5)+"\\s*"+"$";

    public static String getContentType(String formula_body) {
        Pattern textRegex = Pattern.compile(textPattern);
        Pattern formulaRegex = Pattern.compile(formulaPattern);
        Pattern numRegex = Pattern.compile(numPattern);

        Matcher textMatcher = textRegex.matcher(formula_body);
        Matcher formulaMatcher = formulaRegex.matcher(formula_body);
        Matcher numMatcher = numRegex.matcher(formula_body);

        if (formulaMatcher.matches()) {
            return "Formula";
        } else if (numMatcher.matches()) {
            return "Numerical";
        } else if (textMatcher.matches()) {
            return "Text";
        }

        System.out.println("Error content type unsupported" + formula_body);
        return formula_body;
    }
    public static LinkedList<String> tokenize(String formula_body) throws ContentException {
        LinkedList<String> tokens = new LinkedList<>();
        String expectedToken = TokenMatchInfos.get(6);
        while(!formula_body.isEmpty()) {
            Boolean found = false;
            for(String tokeninfo : TokenMatchInfos) {
                //find only if are at start of string! take into account if future strings are a subset of others at start!!
                Pattern p = Pattern.compile('^'+tokeninfo);
                Matcher m = p.matcher(formula_body);
                if (m.find()) {
                    found = true;
                    String token = m.group(0);
                    //System.out.println("tokencomp: \"" + token + "\"");
                    if (token.matches(expectedToken)) { //cell id
                        tokens.add(token);
                    }
                    formula_body = m.replaceFirst("");
                }
            }
            if (!found) {
                throw new ContentException("Tokenize: Invalid token: \"" + formula_body + "\"");
            }
        }
        return tokens;
    }

    private static LinkedList<String> findDistinctElements(LinkedList<String> list1, LinkedList<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> old1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);

        set1.removeAll(set2);
        set2.removeAll(old1);
        set1.addAll(set2);

        return new LinkedList<>(set1);
    }

    private static LinkedList<String> findEqualElements(LinkedList<String> list1, LinkedList<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);

        set1.retainAll(set2);

        return new LinkedList<>(set1);
    }

    public static void updateDependencies(Spreadsheet spreadsheet, NumCoordinate coordinate, LinkedList<String> old_dependencies, LinkedList<String> new_dependencies){
        LinkedList<String> xor_dependencies = findDistinctElements(old_dependencies, new_dependencies);
        LinkedList<String> add_dependencies = findEqualElements(xor_dependencies, new_dependencies);
        LinkedList<String> erase_dependencies = findEqualElements(xor_dependencies, old_dependencies);

        for (String element : add_dependencies) { //OJO
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(element);
            Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet,numCoordinate); //ANY, CAN BE ADDED FOR THE FIRST TIME!
            Dependants dependants = cell.getDependants();
            dependants.addDependant(coordinate);
            cell.setDependants(dependants);
            spreadsheet.cells.addCell(numCoordinate, cell);
        }

        for (String element : erase_dependencies) {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(element);
            Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet,numCoordinate); //ANY, CAN BE ADDED FOR THE FIRST TIME!
            Dependants dependants = cell.getDependants();
            dependants.eraseDependant(coordinate);
            if(dependants.getDependants().size() == 0) {//ERASE CELL IF NEEDED
                Content content = cell.getContent();
                if (!(content instanceof ContentFormula || content instanceof ContentText || content instanceof ContentNumerical)) {
                    spreadsheet.cells.eraseCell(numCoordinate);
                }
            } else {
                cell.setDependants(dependants);
                spreadsheet.cells.addCell(numCoordinate, cell);
            }
        }
    }

    private static boolean hasCircularDependency(Spreadsheet spreadsheet, NumCoordinate numCoordinate, Set<NumCoordinate> localVisited, Set<NumCoordinate> globalVisited) {
        if (localVisited.contains(numCoordinate))
            return true;
        localVisited.add(numCoordinate);
        globalVisited.add(numCoordinate);

        Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet,numCoordinate); //ANY BECAUSE CELLS CAN BE NEW!
        Set<NumCoordinate> dependants = cell.getDependants().getDependants();
        for (NumCoordinate dependant : dependants) {
            if (!globalVisited.contains(dependant)){
                if(hasCircularDependency(spreadsheet, dependant, localVisited, globalVisited))
                    return true;
            } else if (localVisited.contains(dependant)) {
                return true;
            }
        }
        localVisited.remove(numCoordinate);
        return false;
    }

    public static boolean hasCellCircularDependency(Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        Set<NumCoordinate> globalVisited = new HashSet<>();
        Set<NumCoordinate> localVisited = new HashSet<>();
        return hasCircularDependency(spreadsheet, numCoordinate, localVisited, globalVisited);
    }

    public static boolean hasSpreadsheetCircularDependencies(Spreadsheet spreadsheet) {
        Set<NumCoordinate> globalVisited = new HashSet<>();
        Set<NumCoordinate> localVisited = new HashSet<>();
        for (NumCoordinate coordinate: spreadsheet.cells.getCoordinateSet()) {
            if (!globalVisited.contains(coordinate))
                if (hasCircularDependency(spreadsheet, coordinate, localVisited, globalVisited))
                    return true;
        }
        return false;
    }

    //Prerequisite don't recompute if circular dependency
    public static void recomputeCellDependants(Spreadsheet spreadsheet, NumCoordinate numCoordinate) throws ContentException {
        Cell cell = ControllerSpreadsheet.getCellExisting(spreadsheet,numCoordinate);
        Set<NumCoordinate> dependants = cell.getDependants().getDependants();
        for(NumCoordinate dependant : dependants){
            Cell cellDependant = ControllerSpreadsheet.getCellExisting(spreadsheet,dependant);
            String writtenData = ((ContentFormula)cellDependant.getContent()).getWrittenData(); //SHOULD BE FORMULA
            String formulaBody = writtenData.substring(1);
            Float value = Formula.compute(formulaBody, spreadsheet);
            ControllerSpreadsheet.updateFormula(spreadsheet, dependant, writtenData, value);
            recomputeCellDependants(spreadsheet, dependant);// TEMPORAL, LOW PERFORMANCE APPROACH
        }
    }


}
