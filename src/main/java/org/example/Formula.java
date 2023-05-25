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
            "[0-9]+"//093 will be supported... is it fine?
    ));

    public static void tokenize(){
        tokens = new LinkedList<>();
        while(!formula_body.isEmpty()) {
            for(String tokeninfo : TokenMatchInfos) {
                Pattern p = Pattern.compile('^'+tokeninfo);//find only if are at start of string! take into account if future strings are a subset of others at start!!
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
    public static Boolean is_operand(String token){
        if(token.matches("[0-9]+")){
            return true;
        }
        return false;
    }

    public static Boolean is_operator(String token){
        if(token.matches("[+-]")){
            return true;
        }
        return false;
    }

    public static Boolean is_parseable(){ //Need to be improved, not just extended!!(use precedence order!)
        Stack<String> aux_stack = new Stack<>();
        for(int i = 0; i < tokens.size(); ++i){
            String next = tokens.get(i);
            //casos
            if(aux_stack.isEmpty()){
                if(is_operator(next)){ //operator in bad place
                    return false;
                } else {
                    aux_stack.push(next);
                }
            } else {
                String top = aux_stack.peek();
                if(is_operator(top) && is_operator(next)){ //operator in bad place
                    return false;
                }
                if(is_operand(top) && is_operand(next)){ //operand not related to operand
                    return false;
                }
                if(is_operator(top) && is_operand(next)){ //correct, clean data
                    for(int j = 0; j < 2; ++j) {
                        aux_stack.pop();
                    }
                    aux_stack.push(next);
                } else {
                    aux_stack.push(next); //correct, add it
                }
            }
        }

        if(aux_stack.isEmpty()){
            return true;
        } else if(is_operator(aux_stack.peek())) {
            return false;
        } else {
            return true;
        }
    }
}
