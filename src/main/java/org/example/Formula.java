package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula { //1 + 2-4 //The preference in order used to find could be parametrized
    //Tokenizer
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
            "[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?",//093 will be supported... is it fine?
            "([A-Z]+)(\\d+)",
            "\\:",
            "\\;",
            "SUMA" //function
    ));

    public static LinkedList<String> tokenize(String formula_body) throws ContentException {
        LinkedList<String> tokens = new LinkedList<>();
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
                    if (!token.equals(" ")) {
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



    //Parsing
    private static Boolean is_numerical(String token) {
        return is_operand(token) || is_operator(token);
    }
    public static Boolean is_operand(String token){
        return is_number(token) || is_cell_id(token) || is_function(token);
    }

    public static Boolean is_cell_id(String token){
        return token.matches(TokenMatchInfos.get(6));
    }

    public static Boolean is_number(String token){
        return token.matches(TokenMatchInfos.get(5));
    }

    public static Boolean is_function(String token) {
        return token.matches(TokenMatchInfos.get(9));
    }

    public static Boolean is_operator(String token){
        return is_highp_operator(token) || is_lowp_operator(token);
    }

    public static Boolean is_lowp_operator(String token){
        return token.matches(TokenMatchInfos.get(1));
    }

    public static Boolean is_highp_operator(String token){
        return token.matches(TokenMatchInfos.get(2));
    }

    private static Boolean is_claw(String token) {
        return is_open_claw(token) || is_closed_claw(token);
    }

    public static Boolean is_open_claw(String token){
        if(token.matches(TokenMatchInfos.get(3))){
            return true;
        }
        return false;
    }

    public static Boolean is_closed_claw(String token){
        if(token.matches(TokenMatchInfos.get(4))){
            return true;
        }
        return false;
    }

    public static Boolean isColon(String token){
        return token.matches(TokenMatchInfos.get(7));
    }

    public static Boolean isSemicolon(String token){
        return token.matches(TokenMatchInfos.get(8));
    }

    public static Boolean is_parseable(LinkedList<String> tokens) {
        return is_num_balanced(tokens) && is_claw_balanced(tokens) && isParseableFormula(tokens);
    }

    public static Integer is_num_balanced_formula(LinkedList<String> tokens, Integer pos){
        int n = tokens.size(); //size
        int i = pos;
        while(i != n) {
            String next = tokens.get(i);
            if(is_function(next)) {
                int lastFunctionPos = is_num_balanced_formula(tokens, i+1);
                if (lastFunctionPos == n)
                    return n;
                else {
                    i = lastFunctionPos;
                }
            }
            else if(is_closed_claw(next)) {
                return i;
            }
            ++i;
        }
        return n; //END CLAW NOT REACHED
    }

    public static Boolean is_num_balanced(LinkedList<String> tokens){ //Need to be improved, not just extended!!(use precedence order!)
        Stack<String> aux_stack = new Stack<>();
        Boolean hasOperand = false;
        int i = 0;
        while (i < tokens.size()) {
            String next = tokens.get(i);
            if(is_numerical(next)) { //belongs to a set of tokens not treaten here
                if(is_operand(next)) {
                    hasOperand = true;
                }

                if (aux_stack.isEmpty()) {
                    if (is_operator(next)) { //invalid case, operator without first operand
                        return false;
                    } else { //first valid
                        if(is_function(next)) {
                            int lastFunctionPos = is_num_balanced_formula(tokens, i+1);
                            if(lastFunctionPos == tokens.size())
                                return false;
                            else {
                                i = lastFunctionPos; //ALL NESTED FUNCTIONS CHECKED
                            }
                        }

                        aux_stack.push(next);
                    }
                } else {
                    String top = aux_stack.peek();
                    if (is_operator(top) && is_operator(next)) { //invalid case, operator-operator
                        return false;
                    }
                    if (is_operand(top) && is_operand(next)) { //invalid case, operand-operand
                        return false;
                    }
                    if (is_operator(top) && is_operand(next)) { //correct, old operand-operator + add next one (simplification)
                        for (int j = 0; j < 2; ++j) {//erase old operand-operator
                            aux_stack.pop();
                        }
                        if(is_function(next)) {
                            int lastFunctionPos = is_num_balanced_formula(tokens, i+1);
                            if(lastFunctionPos == tokens.size())
                                return false;
                            else {
                                i = lastFunctionPos; //ALL NESTED FUNCTIONS CHECKED
                            }
                        }

                        aux_stack.push(next);//add new operand
                    } else { //operand - operator
                        aux_stack.push(next); //correct, add it
                    }
                }
            }
            ++i;
        }
        if(!hasOperand)
            return false;

        if(aux_stack.isEmpty()){ //only happen if all is empty!
            return true;
        } else if(is_operator(aux_stack.peek())) { //INCOMPLETE operator-operand-operator
            return false;
        }

        return true;
    }

    //Balanced claw rule
    public static Boolean is_claw_balanced(LinkedList<String> tokens){
        Stack<String> aux_stack = new Stack<>();
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(is_claw(next)) {
                if (aux_stack.isEmpty()) {
                    if (is_open_claw(next)) { //first valid
                        aux_stack.push(next);
                    } else if (is_closed_claw(next)) { //invalid case
                        return false;
                    }
                } else {
                    String top = aux_stack.peek();
                    if (is_open_claw(top) && is_closed_claw(next)) { //claw case
                        aux_stack.pop();
                    } else { //next claw
                        aux_stack.push(next);
                    }
                }
            }
        }
        if(aux_stack.isEmpty()){ //final check, is there any open claw left?
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isParseableFormula (LinkedList<String> tokens){
        Stack<String> auxStack = new Stack<>();
        int insideFormula = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if (insideFormula > 0) { //INSIDE FORMULA
                if(is_closed_claw(next)) { //FORMULA CLOSE CLAW
                    insideFormula--;
                }
                /*if(isColon(next) || isSemicolon(next)) {

                }*/
            } else {
                if(isColon(next) || isSemicolon(next)) {
                    return false;
                }
            }

            if (is_function(next)) { //START FORMULA
                ++insideFormula;
                if ((i+1) == tokens.size()) { //FORMULA OPEN CLAW
                    return false;
                } else {
                    String nextNext = tokens.get(i+1);
                    if(!is_open_claw(nextNext)){
                        return false;
                    }
                }
            }
        }
        //Formula no close claw
        if(insideFormula > 0) {
            return false;
        }
        return true;
    }



    //Generate postfix
    private static Integer get_precedence(String token) {
        if (is_highp_operator(token)) return 2;
        if (is_lowp_operator(token)) return 1;
        if (isColon(token)) return 0;
        throw new RuntimeException("Get precedence: precedence not found");
    }

    public static void DisplayPostfix(LinkedList<String> postfix){
        System.out.println("postfix count:" + postfix.size());
        postfix.forEach(pf ->{
            System.out.println(pf);
        });
    }

    public static LinkedList<String> generate_postfix(LinkedList<String> tokens) {
        LinkedList<String> postfix = new LinkedList<>();
        Stack<String> aux_stack = new Stack<>();
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(is_number(next) || is_cell_id(next)) {
                postfix.add(next);
            } else if (is_operator(next)){
                while (!aux_stack.isEmpty() && is_operator(aux_stack.peek()) &&
                        (get_precedence(next) <= get_precedence(aux_stack.peek()))) { //pop and out until top has lower precendence than next
                    postfix.add(aux_stack.pop());
                }
                aux_stack.push(next);
            } else if(is_function(next)) {
                aux_stack.push(next);
            } else if(is_open_claw(next)) {
                aux_stack.push(next);
            } else if (is_closed_claw(next)) { //close clause: pop and out until top is '('
                while (!aux_stack.isEmpty() && !is_open_claw(aux_stack.peek())) { //equals or match??
                    postfix.add(aux_stack.pop());
                }
                aux_stack.pop(); // pop "(", not add ")", See, no checked stack size
            } else if(isSemicolon(next)) {
                while (!aux_stack.isEmpty() && !is_open_claw(aux_stack.peek())) {
                    postfix.add(aux_stack.pop());
                }
            } else if (isColon(next)) {
                while (!aux_stack.isEmpty() && is_operator(aux_stack.peek()) &&
                        (get_precedence(next) <= get_precedence(aux_stack.peek()))) { //pop and out until top has lower precendence than next
                    postfix.add(aux_stack.pop());
                }
                aux_stack.push(next);
            } else {
                System.out.println("Caso else generate postfix para token: " + next);
            }
            //System.out.println("stack: " + i + " elem: " + next + " " + Arrays.toString(aux_stack.toArray()));
        }

        while (!aux_stack.isEmpty()) {
            postfix.add(aux_stack.pop());
        }
        return postfix;
    }

    private static Queue<Integer> getFunctionsNumArgs(LinkedList<String> tokens) {
        Queue<Integer> functionsNumArgs = new LinkedList<>();
        Stack<Integer> pendingArgCnts = new Stack<>();
        int pendingFunctions = 0;
        int localArgCnt = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(is_function(next)) {
                if (pendingFunctions > 0) {
                    pendingArgCnts.push(localArgCnt);
                    localArgCnt = 0;
                }
                pendingFunctions++;
            } else if(is_closed_claw(next)) {
                localArgCnt++; //1 extra token for final argument
                if (pendingFunctions > 0) { //functions being computed
                    functionsNumArgs.add(localArgCnt);
                    if (pendingFunctions > 1) { //Pending functions stored
                        localArgCnt = pendingArgCnts.pop();
                    } else {
                        localArgCnt = 0;
                    }
                    pendingFunctions--;
                }
            /*} else if (isColon(next)) {
                localArgCnt = localArgCnt + 2; //2 extra tokens for an argument which contains it*/
            } else if (isSemicolon(next)) {
                localArgCnt++;
            }
        }
        return functionsNumArgs;
    }


    //Evaluate postfix
    public static float operationCompute (Spreadsheet spreadsheet, String operator, String l_operand, String r_operand) {
        float l_op = 0, r_op = 0;
        if(is_cell_id(l_operand)) { //THIS HAPPENS??
            //throw new RuntimeException("Evaluate postfix: operand: " + l_operand + "should be numerical");
            NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(l_operand);
            Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
            Content content = cell.getContent();
            Float value = (float) 0;
            if (content instanceof ContentFormula) {
                value = ((ContentFormula) content).getValue();
            } else if (content instanceof ContentText) {
                throw new RuntimeException("Cell content text is a formula cell dependency!");
            } else if (content instanceof ContentNumerical) {
                value = ((ContentNumerical) content).getValue();
            }
            l_op = value;
        } else {
            l_op = Float.parseFloat(l_operand);
            //System.out.println("l_op: " + l_op);
        }

        if(is_cell_id(r_operand)) {
            //throw new RuntimeException("Evaluate postfix: operand: " + r_operand + "should be numerical");
            NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(r_operand);
            Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
            Content content = cell.getContent();
            Float value = (float) 0;
            if (content instanceof ContentFormula) {
                value = ((ContentFormula) content).getValue();
            } else if (content instanceof ContentText) {
                throw new RuntimeException("Cell content text is a formula cell dependency!");
            } else if (content instanceof ContentNumerical) {
                value = ((ContentNumerical) content).getValue();
            }
            l_op = value;
        } else {
            r_op = Float.parseFloat(r_operand);
            //System.out.println("r_op: " + r_op);
        }

        float result = switch (operator) {
            case "+" -> l_op + r_op;
            case "-" -> l_op - r_op;
            case "*" -> l_op * r_op;
            case "/" -> l_op / r_op;
            default -> throw new RuntimeException("Evaluate postfix: operator: " + operator + "not supported"); //error should not be needed
        };
        return result;
    }


    public static float functionCompute (Spreadsheet spreadsheet, String function, Integer numArgs, Stack<String> aux_stack) {
        float result = 0;
        for (int i = 0; i < numArgs; ++i) {
            String next = aux_stack.pop();
             if (is_cell_id(next)) {
                NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(next);
                Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
                Content content = cell.getContent();
                Float value = (float) 0;
                if (content instanceof ContentFormula) {
                    value = ((ContentFormula) content).getValue();
                } else if (content instanceof ContentText) {
                    throw new RuntimeException("Cell content text is a formula cell dependency!");
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



    public static float evaluate_postfix(Spreadsheet spreadsheet, LinkedList<String> postfix, LinkedList<String> tokens) { //-1 not suported!
        Stack<String> aux_stack = new Stack<>();
        try {
            Queue<Integer> functionsNumArgs = getFunctionsNumArgs(tokens);
            for (int i = 0; i < postfix.size(); ++i) {
                String next = postfix.get(i);
                if (is_number(next) || is_cell_id(next)) { //MUST BE MODIFIED
                    /*if (is_cell_id(next)) {
                        NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(next);
                        Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
                        Content content = cell.getContent();
                        Float value = (float) 0;
                        if (content instanceof ContentFormula) {
                            value = ((ContentFormula) content).getValue();
                        } else if (content instanceof ContentText) {
                            throw new RuntimeException("Cell content text is a formula cell dependency!");
                        } else if (content instanceof ContentNumerical) {
                            value = ((ContentNumerical) content).getValue();
                        }
                        aux_stack.push(Float.toString(value));
                    } else {*/
                        aux_stack.push(next);
                    //}
                } else if (is_operator(next)) { //should not be necessary, but in functions something here will be modified
                    String down, top;
                    top = aux_stack.pop();
                    down = aux_stack.pop();
                    aux_stack.push(Float.toString(operationCompute(spreadsheet, next, down, top))); //ojo format!!
                } else if (is_function(next)) {
                    aux_stack.push(Float.toString(functionCompute(spreadsheet, next, functionsNumArgs.remove(), aux_stack)));
                } else if (isColon(next)) {
                    String down, top;
                    top = aux_stack.pop();
                    down = aux_stack.pop();
                    NumCoordinate endCoordinate = Translate_coordinate.translate_coordinate_to_int(top);
                    NumCoordinate startCoordinate = Translate_coordinate.translate_coordinate_to_int(down);
                    Integer startRow, endRow, startColum, endColum;
                    startRow = startCoordinate.getNumRow();
                    endRow = endCoordinate.getNumRow();
                    startColum = startCoordinate.getNumColum();
                    endColum = endCoordinate.getNumColum();
                    if(startRow > endRow) throw new RuntimeException("Incorrect rang");
                    if(startColum > endColum) throw new RuntimeException("Incorrect rang");
                    for (int col = startColum; col <= endColum; ++col) {
                        for (int row = startRow; row <= endRow; ++row) {
                            NumCoordinate localCoordinate = new NumCoordinate(row, col);
                            Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, localCoordinate); //To get 0 from empty cells
                            Content content = cell.getContent();
                            Float value = (float) 0;
                            if (content instanceof ContentFormula) {
                                value = ((ContentFormula) content).getValue();
                            } else if (content instanceof ContentText) {
                                throw new RuntimeException("Cell content text is a formula cell dependency!");
                            } else if (content instanceof ContentNumerical) {
                                value = ((ContentNumerical) content).getValue();
                            }
                            aux_stack.push(Float.toString(value));
                        }
                    }
                }
            }
            String next = aux_stack.pop();
            if (is_cell_id(next)) {
                NumCoordinate numCoordinate = Translate_coordinate.translate_coordinate_to_int(next);
                Cell cell = ControllerSpreadsheet.getCellAny(spreadsheet, numCoordinate); //To get 0 from empty cells
                Content content = cell.getContent();
                Float value = (float) 0;
                if (content instanceof ContentFormula) {
                    value = ((ContentFormula) content).getValue();
                } else if (content instanceof ContentText) {
                    throw new RuntimeException("Cell content text is a formula cell dependency!");
                } else if (content instanceof ContentNumerical) {
                    value = ((ContentNumerical) content).getValue();
                }
                return value;
            } else {
                return Float.parseFloat(next);
            }
        } catch (Exception e) {
            throw new RuntimeException("Evaluate postfix: " + e.getMessage());
        }
    }


    public static Float compute(String formula_body, Spreadsheet spreadsheet) throws ContentException {
        float value = 0;
        try {
            LinkedList<String> tokens = tokenize(formula_body);
            if (!is_parseable(tokens)) {
                throw new RuntimeException("No parseable tokens");
            }
            LinkedList<String> postfix = generate_postfix(tokens);
            value = evaluate_postfix(spreadsheet, postfix, tokens);
        } catch (Exception e) {
            if (e instanceof ContentException) {
                throw new ContentException("Compute: " + e.getMessage());
            } else {
                throw new RuntimeException("Compute: " + e.getMessage());
            }
        }
        return value;
    }


}