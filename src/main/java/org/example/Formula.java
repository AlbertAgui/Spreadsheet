package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula { //1 + 2-4 //The preference in order used to find could be parametrized
    //Tokenizer
    public static String formula_body;
    public static LinkedList<String> tokens;

    public static void setFormula_body(String formula_body) {
        Formula.formula_body = formula_body;
    }

    public static void DisplayTokens(){
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
            "[0-9]+"//093 will be supported... is it fine?
    ));

    public static void tokenize(){
        tokens = new LinkedList<>();
        while(!formula_body.isEmpty()) {
            for(String tokeninfo : TokenMatchInfos) {
                //find only if are at start of string! take into account if future strings are a subset of others at start!!
                Pattern p = Pattern.compile('^'+tokeninfo);
                Matcher m = p.matcher(formula_body);
                if (m.find()) {
                    String token = m.group(0);
                    System.out.println("tokencomp: \"" + token + "\"");
                    if (!token.equals(" ")) {
                        tokens.add(token);
                    }

                    formula_body = m.replaceFirst("");
                    //break;
                }
                //break;
            }
        }
    }


    //Parsing
    private static Boolean is_numerical(String token) {
        return is_operand(token) || is_operator(token);
    }
    public static Boolean is_operand(String token){
        if(token.matches("[0-9]+")){
            return true;
        }
        return false;
    }

    public static Boolean is_operator(String token){
        if(token.matches("[+-]") || token.matches("[*/]")){
            return true;
        }
        return false;
    }

    private static Boolean is_claw(String token) {
        return is_open_claw(token) || is_closed_claw(token);
    }

    public static Boolean is_open_claw(String token){
        if(token.matches("\\(")){
            return true;
        }
        return false;
    }

    public static Boolean is_closed_claw(String token){
        if(token.matches("\\)")){
            return true;
        }
        return false;
    }

    public static Boolean is_num_balanced(){ //Need to be improved, not just extended!!(use precedence order!)
        Stack<String> aux_stack = new Stack<>();
        for(int i = 0; i < tokens.size(); ++i){
            String next = tokens.get(i);
            if(is_numerical(next)) { //belongs to a set of tokens not treaten here
                if (aux_stack.isEmpty()) {
                    if (is_operator(next)) { //invalid case, operator without first operand
                        return false;
                    } else { //first valid
                        aux_stack.push(next);
                    }
                } else {
                    String top = aux_stack.peek();
                    if (is_operator(top) && is_operator(next)) { //invalid case, operator without second operand
                        return false;
                    }
                    if (is_operand(top) && is_operand(next)) { //invalid case, operand without operator
                        return false;
                    }
                    if (is_operator(top) && is_operand(next)) { //correct, old operand-operator + add next one (simplification)
                        for (int j = 0; j < 2; ++j) {
                            aux_stack.pop();
                        }
                        aux_stack.push(next);
                    } else { //next token, normal case
                        aux_stack.push(next); //correct, add it
                    }
                }
            }
        }
        if(aux_stack.isEmpty()){ //only happen if all is empty!
            return true;
        } else if(is_operator(aux_stack.peek())) { //incomplet operator-operand-operator
            return false;
        } else { //normal case, operand at top, only 1
            return true;
        }
    }

    //Balanced claw rule
    public static Boolean is_claw_balanced(){
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

    public static Boolean is_parseable(){
        return is_num_balanced() && is_claw_balanced();
    }

    //Generate postfix
    private static LinkedList<String> postfix;
    public static void

    //Evaluate postfix
}
