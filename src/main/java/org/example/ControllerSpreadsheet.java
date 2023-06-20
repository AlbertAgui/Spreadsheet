package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerSpreadsheet {

    public static final List<String> TokenMatchInfos = new ArrayList<>(Arrays.asList( //static="class instance, unique", final="static, constant"
            "\s",//is this needed?
            "[+-]",
            "[*/]",
            "\\(",
            "\\)",
            "[-+]?\\d*\\.?\\d+",//093 will be supported... is it fine?
            "([A-Z]+)(\\d+)"
    ));

    public static LinkedList<String> tokenize(String formula_body) {
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
                throw new RuntimeException("Tokenize: Invalid token: \"" + formula_body + "\"");
            }
        }
        return tokens;
    }


    private static String textPattern = "^(?!=).*$";
    private static String formulaPattern = "^=.*$";
    private static String numPattern = "^\s*[-+]?\\d*\\.?\\d+\s*$";

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
            Cell cell = getCellAny(spreadsheet,numCoordinate); //ANY, CAN BE ADDED FOR THE FIRST TIME!
            Dependants dependants = cell.getDependants();
            dependants.addDependant(coordinate);
            cell.setDependants(dependants);
            spreadsheet.cells.addCell(numCoordinate, cell);
        }

        for (String element : erase_dependencies) {
            NumCoordinate numCoordinate;
            numCoordinate = Translate_coordinate.translate_coordinate_to_int(element);
            Cell cell = getCellAny(spreadsheet,numCoordinate); //ANY, CAN BE ADDED FOR THE FIRST TIME!
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

    public static void updateFormula(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String writtenContent, float value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentFormula)) {
            new_content = new ContentFormula();
        }
        ((ContentFormula) new_content).setWrittenData(writtenContent);
        ((ContentFormula) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }

    public static void updateText(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentText)) {
            new_content = new ContentText();
        }
        ((ContentText) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }

    public static void updateNumerical(Spreadsheet spreadsheet, NumCoordinate numCoordinate, float value){
        Cell cell = getCellAny(spreadsheet, numCoordinate);
        Content new_content = cell.getContent();
        if (!(new_content instanceof ContentNumerical)) {
            new_content = new ContentNumerical();
        }
        ((ContentNumerical) new_content).setValue(value);
        cell.setContent(new_content);
        spreadsheet.cells.addCell(numCoordinate, cell);
    }


    private static void recomputeCell(Spreadsheet spreadsheet, NumCoordinate numCoordinate, Queue<NumCoordinate> stack, Set<NumCoordinate> visited) {
        visited.add(numCoordinate);
        Cell cell = ControllerSpreadsheet.getCellExisting(spreadsheet,numCoordinate);
        Content content = cell.getContent();
        if(content instanceof ContentFormula) {
            String input = ((ContentFormula) content).getWrittenData();
            String body = input.substring(1);
            LinkedList <String> dependencies = tokenize(body);
            if(dependencies.isEmpty()) {
                stack.add(numCoordinate);
            } else {
                for (String dependency : dependencies) {
                    NumCoordinate coordinate;
                    coordinate = Translate_coordinate.translate_coordinate_to_int(dependency);
                    if(!visited.contains(coordinate)) {
                        recomputeCell(spreadsheet, coordinate, stack, visited);
                    }
                }
                stack.add(numCoordinate);
            }
        } /*else if (content instanceof ContentText) {
            throw new Exception("load computing spreadsheet text dependency, this shouldn't happen");
        }*/
    }


    public static void recomputeSpreadsheet(Spreadsheet spreadsheet) throws ContentException {
        Set<NumCoordinate> visited = new HashSet<>();
        for (NumCoordinate coordinate: spreadsheet.cells.getCoordinateSet()) {
            if(!visited.contains(coordinate)) {
                Queue<NumCoordinate> queue = new LinkedList<>();
                recomputeCell(spreadsheet, coordinate, queue, visited);
                while(!queue.isEmpty()) {
                    NumCoordinate numCoordinate = queue.remove();
                    Cell cell = ControllerSpreadsheet.getCellExisting(spreadsheet, numCoordinate);
                    String writtenData = ((ContentFormula)cell.getContent()).getWrittenData(); //SHOULD BE FORMULA
                    String formulaBody = writtenData.substring(1);
                    Float value = Formula.compute(formulaBody, spreadsheet);
                    updateFormula(spreadsheet, numCoordinate, writtenData, value);
                }
            }
        }
    }

    //Prerequisite don't recompute if circular dependency
    private static void recomputeCellDependants(Spreadsheet spreadsheet, NumCoordinate numCoordinate) throws ContentException {
        Cell cell = ControllerSpreadsheet.getCellExisting(spreadsheet,numCoordinate);
        Set<NumCoordinate> dependants = cell.getDependants().getDependants();
        for(NumCoordinate dependant : dependants){
            Cell cellDependant = ControllerSpreadsheet.getCellExisting(spreadsheet,dependant);
            String writtenData = ((ContentFormula)cellDependant.getContent()).getWrittenData(); //SHOULD BE FORMULA
            String formulaBody = writtenData.substring(1);
            Float value = Formula.compute(formulaBody, spreadsheet);
            updateFormula(spreadsheet, dependant, writtenData, value);
            recomputeCellDependants(spreadsheet, dependant);// TEMPORAL, LOW PERFORMANCE APPROACH
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



    public static void editCell(Spreadsheet spreadsheet, NumCoordinate numCoordinate, String input) { //WORKING
        try {
            String inputType = getContentType(input);
            String formulaBody = input.substring(1);
            Cell old_cell = null;
            Content old_content = null;
            LinkedList<String> old_dependencies = null;
            LinkedList<String> new_dependencies = null;
            float newValue = 0;
            switch (inputType) {
                case "Formula":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    newValue = Formula.compute(formulaBody, spreadsheet);
                    new_dependencies = tokenize(formulaBody);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = tokenize(old_body);
                        } else {
                            old_dependencies = new LinkedList<>();
                        }
                    } else {
                        old_dependencies = new LinkedList<>();
                    }
                    updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                    updateFormula(spreadsheet, numCoordinate, input, newValue);
                    if (hasCellCircularDependency(spreadsheet, numCoordinate)) {
                        updateDependencies(spreadsheet, numCoordinate, new_dependencies, old_dependencies); //redo dependencies

                        //UNDO UPDATE FORMULA
                        if (old_content == null) { //NOT BEST SOLUTION, ALL CELL SHOULD BE COPIED RECURSIVELLY, NOT ROBUST
                            spreadsheet.cells.eraseCell(numCoordinate);
                        } else {
                            Cell recoverCell = spreadsheet.cells.getCell(numCoordinate);
                            recoverCell.setContent(old_content);
                            spreadsheet.cells.addCell(numCoordinate, recoverCell);
                        }
                        throw new RuntimeException("Circular dependency");
                    } else {
                        //recompute values
                        recomputeCellDependants(spreadsheet, numCoordinate);
                    }

                    break;
                case "Text":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = tokenize(old_body);
                            new_dependencies = new LinkedList<>();
                            updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                        }
                    }
                    //update written, value
                    updateText(spreadsheet, numCoordinate, input);
                    //recompute values
                    recomputeCellDependants(spreadsheet, numCoordinate);
                    break;
                case "Numerical":
                    old_cell = getCellNull(spreadsheet, numCoordinate);
                    if (old_cell != null) {
                        old_content = old_cell.getContent();
                        if (old_content instanceof ContentFormula) {
                            String old_writtencontent = ((ContentFormula) old_content).getWrittenData();
                            String old_body = old_writtencontent.substring(1);
                            old_dependencies = tokenize(old_body);
                            new_dependencies = new LinkedList<>();
                            updateDependencies(spreadsheet, numCoordinate, old_dependencies, new_dependencies);
                        }
                    }
                    //update written, value
                    String inputTrim = input.trim(); //ERASE SPACES
                    newValue = Float.parseFloat(inputTrim);
                    updateNumerical(spreadsheet, numCoordinate, newValue);
                    //recompute values
                    recomputeCellDependants(spreadsheet, numCoordinate);
                    break;
                default:
                    System.out.println("No concrete content factory method for " + inputType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error Edit Cell: " + e.getMessage());
        }
    }


    //UTILS
    //GET CELL
    public static Cell getCellExisting (Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        Cell cell = spreadsheet.cells.getCell(numCoordinate);
        if (cell == null)
            throw new RuntimeException("Cell not found: colum: " + numCoordinate.getNumColum() + ", row: " + numCoordinate.getNumRow());
        return cell;
    }

    public static Cell getCellNull (Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        return spreadsheet.cells.getCell(numCoordinate);
    }

    public static Cell getCellAny (Spreadsheet spreadsheet, NumCoordinate numCoordinate) {
        Cell cell = spreadsheet.cells.getCell(numCoordinate);
        if (cell == null)
            cell = new Cell();
        return cell;
    }

    //ADD CELL

}
