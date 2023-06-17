package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerSpreadsheet {
    public static void DisplayTokens(LinkedList<String> tokens){
        System.out.println("tokens count:" + tokens.size());
        tokens.forEach(tk ->{
            System.out.println(tk);
        });
    }

    public static final List<String> TokenMatchInfos = new ArrayList<>(Arrays.asList( //static="class instance, unique", final="static, constant"
            "\s",//is this needed?
            "[+-]",
            "[*/]",
            "\\(",
            "\\)",
            "[0-9]+",//093 will be supported... is it fine?
            "([A-Z]+)(\\d+)"
    ));

    public static LinkedList<String> tokenize(String formula_body){
        LinkedList<String> tokens = new LinkedList<>();
        String regex = "([A-Z]+)(\\d+)";
        while(!formula_body.isEmpty()) {
            for(String tokeninfo : TokenMatchInfos) {
                //find only if are at start of string! take into account if future strings are a subset of others at start!!
                Pattern p = Pattern.compile('^'+tokeninfo);
                Matcher m = p.matcher(formula_body);
                if (m.find()) {
                    String token = m.group(0);
                    //System.out.println("tokencomp: \"" + token + "\"");
                    if (token.matches(regex)) {
                        tokens.add(token);
                    }

                    formula_body = m.replaceFirst("");
                    //break;
                }
                //break;
            }
        }
        return tokens;
    }


    private static String textPattern = "^(?!=).*$";
    private static String formulaPattern = "^=.*";
    private static String numPattern = "^\\d+$";

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
            Cell cell = spreadsheet.cells.getCell(numCoordinate);
            cell.addDependant(coordinate);
            spreadsheet.cells.addCell(numCoordinate, cell);
        }

        for (String element : erase_dependencies) {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(element);
            Cell cell = spreadsheet.cells.getCell(numCoordinate);
            cell.eraseDependant(coordinate);
            spreadsheet.cells.addCell(numCoordinate, cell);
        }
    }

    public static void updateFormula(Spreadsheet spreadsheet, NumCoordinate coordinate, String input, float value){
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Content new_content = new_cell.getContent();
        if (!(new_content instanceof ContentFormula)) {
            new_content = new ContentFormula();
        }
        ((ContentFormula) new_content).setWrittenData(input);
        ((ContentFormula) new_content).setValue(value);
        new_cell.setContent(new_content);
        spreadsheet.cells.addCell(coordinate, new_cell);
    }

    public static void updateText(Spreadsheet spreadsheet, NumCoordinate coordinate, String value){
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Content new_content = new_cell.getContent();
        if (!(new_content instanceof ContentText)) {
            new_content = new ContentText();
        }
        ((ContentText) new_content).setValue(value);
        new_cell.setContent(new_content);
        spreadsheet.cells.addCell(coordinate, new_cell);
    }

    public static void updateNumerical(Spreadsheet spreadsheet, NumCoordinate coordinate, float value){
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Content new_content = new_cell.getContent();
        if (!(new_content instanceof ContentNumerical)) {
            new_content = new ContentNumerical();
        }
        ((ContentNumerical) new_content).setValue(value);
        new_cell.setContent(new_content);
        spreadsheet.cells.addCell(coordinate, new_cell);
    }

    private static void recomputeDependant(Spreadsheet spreadsheet, NumCoordinate coordinate) {
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Set<NumCoordinate> dependants = new_cell.getDependants();
        for(NumCoordinate dependant : dependants){
            Cell cell = spreadsheet.cells.getCell(dependant);
            String input = ((ContentFormula)cell.getContent()).getWrittenData();
            String body = input.replace("=", "");
            Result result = Formula.compute(body, spreadsheet);
            if(!result.getSuccess()){
                System.out.println("Error recomputing dependents formula");
                return;
            }
            updateFormula(spreadsheet, dependant, input, (Float) result.getValue());
        }
    }

    //Prerequisite don't recompute if circular dependency
    private static void recomputeCellDependants(Spreadsheet spreadsheet, NumCoordinate coordinate) {
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Set<NumCoordinate> dependants = new_cell.getDependants();
        for(NumCoordinate dependant : dependants){
            Cell cell = spreadsheet.cells.getCell(dependant);
            String input = ((ContentFormula)cell.getContent()).getWrittenData();
            String body = input.replace("=", "");
            Result result = Formula.compute(body, spreadsheet);
            if(!result.getSuccess()){
                System.out.println("Error recomputing cell dependents formula");
                return;
            }
            updateFormula(spreadsheet, dependant, input, (Float) result.getValue());
            recomputeCellDependants(spreadsheet, dependant);// TEMPORAL, LOW PERFORMANCE APPROACH
        }
    }

    private static boolean hasCircularDependency(Spreadsheet spreadsheet, NumCoordinate coordinate, Set<NumCoordinate> localVisited, Set<NumCoordinate> globalVisited) {
        if (localVisited.contains(coordinate))
            return true;
        localVisited.add(coordinate);
        globalVisited.add(coordinate);

        Set<NumCoordinate> dependants = spreadsheet.cells.getCell(coordinate).getDependants();
        for (NumCoordinate dependant : dependants) {
            if (!globalVisited.contains(dependant)){
                if(hasCircularDependency(spreadsheet, dependant, localVisited, globalVisited))
                    return true;
            } else if (localVisited.contains(dependant)) {
                return true;
            }
        }
        localVisited.remove(coordinate);
        return false;
    }

    public static boolean hasCellCircularDependency(Spreadsheet spreadsheet, NumCoordinate coordinate) {
        Set<NumCoordinate> globalVisited = new HashSet<>();
        Set<NumCoordinate> localVisited = new HashSet<>();
        return hasCircularDependency(spreadsheet, coordinate, localVisited, globalVisited);
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



    public static void editCell(Spreadsheet spreadsheet, NumCoordinate coordinate, String input) { //WORKING
        String inputType = getContentType(input);
        String body = input.replace("=", "");
        switch (inputType) {
            case "Formula" :
                if(spreadsheet.cells.containsCell(coordinate)) {
                    Cell old_cell = spreadsheet.cells.getCell(coordinate);
                    Content old_content = old_cell.getContent();
                    if(old_content instanceof ContentFormula) {
                        Result result = Formula.compute(body, spreadsheet);
                        if(!result.getSuccess()){
                            System.out.println("Error computing formula");
                            return;
                        }
                        float new_value = (Float) result.getValue();
                        String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                        String old_body = old_writtencontent.replace("=", "");
                        LinkedList<String> old_dependencies = tokenize(old_body);
                        LinkedList<String> new_dependencies = tokenize(body);
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        if(hasCellCircularDependency(spreadsheet, coordinate)){
                            System.out.println("has Circular dependency " + inputType);
                            updateDependencies(spreadsheet, coordinate, new_dependencies, old_dependencies); //redo dependencies
                        } else {
                            //update written, value
                            updateFormula(spreadsheet, coordinate, input, new_value);
                            //recompute values
                            recomputeCellDependants(spreadsheet, coordinate);
                        }
                    } else {
                        Result result = Formula.compute(body, spreadsheet);
                        if(!result.getSuccess()){
                            System.out.println("Error computing formula");
                            return;
                        }
                        float new_value = (Float) result.getValue();
                        LinkedList<String> old_dependencies = new LinkedList<>();
                        LinkedList<String> new_dependencies = tokenize(body);
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        if(hasCellCircularDependency(spreadsheet, coordinate)){
                            System.out.println("has Circular dependency " + inputType);
                            updateDependencies(spreadsheet, coordinate, new_dependencies, old_dependencies); //redo dependencies
                        } else {
                            //update written, value
                            updateFormula(spreadsheet, coordinate, input, new_value);
                            //recompute values
                            recomputeCellDependants(spreadsheet, coordinate);
                        }
                    }
                } else {
                    Result result = Formula.compute(body, spreadsheet);
                    if(!result.getSuccess()){
                        System.out.println("Error computing formula");
                        return;
                    }
                    float new_value = (Float) result.getValue();
                    LinkedList<String> old_dependencies = new LinkedList<>();
                    LinkedList<String> new_dependencies = tokenize(body);
                    updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                    if(hasCellCircularDependency(spreadsheet, coordinate)){
                        System.out.println("has Circular dependency " + inputType);
                        updateDependencies(spreadsheet, coordinate, new_dependencies, old_dependencies); //redo dependencies
                    } else {
                        //update written, value
                        updateFormula(spreadsheet, coordinate, input, new_value);
                        //recompute values
                        recomputeCellDependants(spreadsheet, coordinate);
                    }
                }
                break;
            case "Text" :
                if(spreadsheet.cells.containsCell(coordinate)) {
                    Cell old_cell = spreadsheet.cells.getCell(coordinate);
                    Content old_content = old_cell.getContent();
                    if (old_content instanceof ContentFormula) {
                        String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                        String old_body = old_writtencontent.replace("=", "");
                        LinkedList<String> old_dependencies = tokenize(old_body);
                        LinkedList<String> new_dependencies = new LinkedList<>();
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        //update written, value
                        updateText(spreadsheet, coordinate, body);
                        //recompute values
                        recomputeCellDependants(spreadsheet, coordinate);
                    } else {
                        //update written, value
                        updateText(spreadsheet, coordinate, body);
                        //recompute values
                        recomputeCellDependants(spreadsheet, coordinate);
                    }
                } else {
                    //update written, value
                    updateText(spreadsheet, coordinate, body);
                    //recompute values
                    recomputeCellDependants(spreadsheet, coordinate);
                }
                break;
            case "Numerical" :
                if(spreadsheet.cells.containsCell(coordinate)) {
                    Cell old_cell = spreadsheet.cells.getCell(coordinate);
                    Content old_content = old_cell.getContent();
                    if (old_content instanceof ContentFormula) {
                        String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                        String old_body = old_writtencontent.replace("=", "");
                        LinkedList<String> old_dependencies = tokenize(old_body);
                        LinkedList<String> new_dependencies = new LinkedList<>();
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        //update written, value
                        float new_value = Float.parseFloat(body);
                        updateNumerical(spreadsheet, coordinate, new_value);
                        //recompute values
                        recomputeCellDependants(spreadsheet, coordinate);
                    } else {
                        //update written, value
                        float new_value = Float.parseFloat(body);
                        updateNumerical(spreadsheet, coordinate, new_value);
                        //recompute values
                        recomputeCellDependants(spreadsheet, coordinate);
                    }
                } else {
                    //update written, value
                    float new_value = Float.parseFloat(body);
                    updateNumerical(spreadsheet, coordinate, new_value);
                    //recompute values
                    recomputeCellDependants(spreadsheet, coordinate);
                }
                break;
            default:
                System.out.println("No concrete content factory method for " + inputType);
        }
    }
}
