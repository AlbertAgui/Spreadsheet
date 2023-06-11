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

    private static String getContentType(String formula_body) {
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
        System.out.println("Error content type unsoported" + formula_body);
        return null;
    }


    private static LinkedList<String> findDistinctElements(LinkedList<String> list1, LinkedList<String> list2) {
        LinkedList<String> list = new LinkedList<>();

        list.addAll(list1);

        LinkedList<String> result = new LinkedList<>();

        for (String element : list2) {
            if (!list.contains(element)) {
                result.add(element);
            }
        }

        return result;
    }

    private static LinkedList<String> findEqualElements(LinkedList<String> list1, LinkedList<String> list2) {
        LinkedList<String> list = new LinkedList<>();

        list.addAll(list1);

        LinkedList<String> result = new LinkedList<>();

        for (String element : list2) {
            if (list.contains(element)) {
                result.add(element);
            }
        }

        return result;
    }


    private static void updateDependencies(Spreadsheet spreadsheet, NumCoordinate coordinate, LinkedList<String> old_dependencies, LinkedList<String> new_dependencies){
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

    private static void updateFormula(Spreadsheet spreadsheet, NumCoordinate coordinate, String input, float value){
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

    private static void updateText(Spreadsheet spreadsheet, NumCoordinate coordinate, String value){
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Content new_content = new_cell.getContent();
        if (!(new_content instanceof ContentText)) {
            new_content = new ContentText();
        }
        ((ContentText) new_content).setValue(value);
        new_cell.setContent(new_content);
        spreadsheet.cells.addCell(coordinate, new_cell);
    }

    private static void updateNumerical(Spreadsheet spreadsheet, NumCoordinate coordinate, float value){
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Content new_content = new_cell.getContent();
        if (!(new_content instanceof ContentNumerical)) {
            new_content = new ContentNumerical();
        }
        ((ContentNumerical) new_content).setValue(value);
        new_cell.setContent(new_content);
        spreadsheet.cells.addCell(coordinate, new_cell);
    }

    private static void recomputeDependants(Spreadsheet spreadsheet, NumCoordinate coordinate) {
        Cell new_cell = spreadsheet.cells.getCell(coordinate);
        Set<NumCoordinate> dependants = new_cell.getDependants();
        for(NumCoordinate dependant : dependants){
            Cell cell = spreadsheet.cells.getCell(dependant);
            String input = ((ContentFormula)cell.getContent()).getWrittenData();
            String body = input.replace("=", "");
            float new_value = Formula.compute(body, spreadsheet);
            updateFormula(spreadsheet, dependant, input, new_value);
        }
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
                        float new_value = Formula.compute(body, spreadsheet);
                        String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                        String old_body = old_writtencontent.replace("=", "");
                        LinkedList<String> old_dependencies = tokenize(old_body);
                        LinkedList<String> new_dependencies = tokenize(body);
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        //update written, value
                        updateFormula(spreadsheet, coordinate, input, new_value);
                        //recompute values
                        recomputeDependants(spreadsheet, coordinate);
                    } else {
                        float new_value = Formula.compute(body, spreadsheet);
                        LinkedList<String> old_dependencies = new LinkedList<>();
                        LinkedList<String> new_dependencies = tokenize(body);
                        updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                        //update written, value
                        updateFormula(spreadsheet, coordinate, input, new_value);
                        //recompute values
                        recomputeDependants(spreadsheet, coordinate);
                    }
                } else {
                    float new_value = Formula.compute(body, spreadsheet);
                    LinkedList<String> old_dependencies = new LinkedList<>();
                    LinkedList<String> new_dependencies = tokenize(body);
                    updateDependencies(spreadsheet, coordinate, old_dependencies, new_dependencies);
                    //update written, value
                    updateFormula(spreadsheet, coordinate, input, new_value);
                    //recompute values
                    recomputeDependants(spreadsheet, coordinate);
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
                        recomputeDependants(spreadsheet, coordinate);
                    } else {
                        //update written, value
                        updateText(spreadsheet, coordinate, body);
                        //recompute values
                        recomputeDependants(spreadsheet, coordinate);
                    }
                } else {
                    //update written, value
                    updateText(spreadsheet, coordinate, body);
                    //recompute values
                    recomputeDependants(spreadsheet, coordinate);
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
                        recomputeDependants(spreadsheet, coordinate);
                    } else {
                        //update written, value
                        float new_value = Float.parseFloat(body);
                        updateNumerical(spreadsheet, coordinate, new_value);
                        //recompute values
                        recomputeDependants(spreadsheet, coordinate);
                    }
                } else {
                    //update written, value
                    float new_value = Float.parseFloat(body);
                    updateNumerical(spreadsheet, coordinate, new_value);
                    //recompute values
                    recomputeDependants(spreadsheet, coordinate);
                }
                break;
            default:
                System.out.println("No concrete content factory method for " + inputType);
        }
    }
}
