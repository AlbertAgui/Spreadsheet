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
                    //System.out.println("tokencomp: \"" + token + "\"");
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
        return is_highp_operator(token) || is_lowp_operator(token);
    }

    public static Boolean is_lowp_operator(String token){
        return token.matches("[+-]");
    }

    public static Boolean is_highp_operator(String token){
        return token.matches("[*/]");
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

    public static Boolean is_parseable() {
        return is_num_balanced() && is_claw_balanced();
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



    //Generate postfix
    private static LinkedList<String> postfix;

    private static Integer get_precedence(String token) {
        if (is_closed_claw(token)) return 3;
        if (is_highp_operator(token)) return 2;
        if (is_lowp_operator(token)) return 1;
        if (is_open_claw(token)) return 0;
        return -1; //error, will be modified
    }

    public static void DisplayPostfix(){
        System.out.println("postfix count:" + postfix.size());
        postfix.forEach(pf ->{
            System.out.println(pf);
        });
    }

    public static void generate_postfix() {
        postfix = new LinkedList<>();
        Stack<String> aux_stack = new Stack<>();
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(is_operand(next)) {
                postfix.add(next);
            }
            else if (aux_stack.isEmpty()) {
                aux_stack.push(next);
            } else {
                String top = aux_stack.peek();
                if(get_precedence(next) == 0){ //open clause
                    aux_stack.push(next);
                } else if (get_precedence(next) == 3) { //close clause: pop and out until top is '('
                    while (!top.equals("(")) { //equals or match??
                        aux_stack.pop();
                        postfix.add(top);
                        if(!aux_stack.isEmpty()) { //should not be necessary
                            top = aux_stack.peek(); //gets value
                        } else {
                            break;
                        }
                    }
                    aux_stack.pop(); // pop "(", not add ")", See, no checked stack size
                } else if(get_precedence(next) > get_precedence(top)) {
                    aux_stack.push(next);
                } else {
                    while (get_precedence(next) <= get_precedence(top)) { //pop and out until top has lower precendence than next
                        aux_stack.pop();
                        postfix.add(top);
                        if(!aux_stack.isEmpty()) { //should not be necessary
                            top = aux_stack.peek(); //gets value
                        } else {
                            break;
                        }
                    }
                    aux_stack.push(next);
                }
            }
            //System.out.println("stack: " + i + " elem: " + next + " " + Arrays.toString(aux_stack.toArray()));
        }
        while (!aux_stack.isEmpty()) {
            postfix.add(aux_stack.pop());
        }
    }




    //Evaluate postfix
    public static float operate(String operator, String l_operand, String r_operand) {
        float l_op = Float.parseFloat(l_operand);
        float r_op = Float.parseFloat(r_operand);
        float result = switch (operator) {
            case "+" -> l_op + r_op;
            case "-" -> l_op - r_op;
            case "*" -> l_op * r_op;
            case "/" -> l_op / r_op;
            default -> -1; //error should not be needed
        };
        return result;
    }

    public static float evaluate_postfix() { //-1 not suported!
        int value;
        Stack<String> aux_stack = new Stack<>();
        for (int i = 0; i < postfix.size(); ++i) {
            String next = postfix.get(i);
            if(is_operand(next)) {
                aux_stack.push(next);
            } else if (is_operator(next)) { //should not be necessary, but in functions something here will be modified
                String down, top;
                top = aux_stack.pop();
                down = aux_stack.pop();
                aux_stack.push(Float.toString(operate(next, down, top))); //ojo format!!
            }
            //System.out.println("stack: " + i + " elem: " + next + " " + Arrays.toString(aux_stack.toArray()));
        }
        return Float.parseFloat(aux_stack.pop());
    }

    public static float compute(String formula_body) {
        setFormula_body(formula_body);
        tokenize();
        if (is_parseable()) {
            //System.out.println("Correct!");
            generate_postfix();
            return evaluate_postfix();
        } else {
            System.out.println("No parseable formula!");
            return 0;
        }
    }

}
