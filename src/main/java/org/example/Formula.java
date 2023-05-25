package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
            "\s",
            "[0-9]+",//093 will be supported... is it fine?
            "[+-]"
    ));

    public static void tokenize(){
        tokens = new LinkedList<>();
        while(!formula_body.isEmpty()) {
            TokenMatchInfos.forEach(tokeninfo -> {
                Pattern p = Pattern.compile(tokeninfo);
                Matcher m = p.matcher(formula_body);
                if (m.find()) {
                    String token = m.group(0);
                    System.out.println("tokencomp: \"" + token + "\"");
                    if (!token.equals(" ")) {
                        tokens.add(token);
                    }

                    formula_body = m.replaceFirst("");
                }
            });
        }
    }

    //Parsing
}
