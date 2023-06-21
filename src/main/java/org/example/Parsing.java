package org.example;

import java.util.LinkedList;
import java.util.Stack;

public class Parsing {

    private static Boolean is_numerical(String token) {
        return is_operand(token) || is_operator(token);
    }
    public static Boolean is_operand(String token){
        return is_number(token) || is_cell_id(token) || is_function(token);
    }

    public static Boolean is_cell_id(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(6));
    }

    public static Boolean is_number(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(5));
    }

    public static Boolean is_function(String token) {
        return token.matches(ContentTools.TokenMatchInfos.get(9)) || token.matches(ContentTools.TokenMatchInfos.get(10)) || token.matches(ContentTools.TokenMatchInfos.get(11)) || token.matches(ContentTools.TokenMatchInfos.get(12));
    }

    public static Boolean is_operator(String token){
        return is_highp_operator(token) || is_lowp_operator(token);
    }

    public static Boolean is_lowp_operator(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(1));
    }

    public static Boolean is_highp_operator(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(2));
    }

    private static Boolean is_claw(String token) {
        return is_open_claw(token) || is_closed_claw(token);
    }

    public static Boolean is_open_claw(String token){
        if(token.matches(ContentTools.TokenMatchInfos.get(3))){
            return true;
        }
        return false;
    }

    public static Boolean is_closed_claw(String token){
        if(token.matches(ContentTools.TokenMatchInfos.get(4))){
            return true;
        }
        return false;
    }

    public static Boolean isColon(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(7));
    }

    public static Boolean isSemicolon(String token){
        return token.matches(ContentTools.TokenMatchInfos.get(8));
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
                if(isColon(next)) {
                    if (i == tokens.size())
                        return false;
                    else{
                        if(!(is_cell_id(tokens.get(i-1)) && is_cell_id(tokens.get(i+1))))
                            return false;
                    }
                } else if(isSemicolon(next)) {
                    if (i == tokens.size())
                        return false;
                    else if(is_open_claw(tokens.get(i-1)) || is_closed_claw(tokens.get(i+1))){
                        return false;
                    }
                }
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
}
