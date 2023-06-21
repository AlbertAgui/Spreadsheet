package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenize {
    public static void DisplayTokens(LinkedList<String> tokens){
        System.out.println("tokens count:" + tokens.size());
        tokens.forEach(tk ->{
            System.out.println(tk);
        });
    }

    public static LinkedList<String> tokenize(String formula_body) throws ContentException {
        LinkedList<String> tokens = new LinkedList<>();
        while(!formula_body.isEmpty()) {
            Boolean found = false;
            for(String tokeninfo : ContentTools.TokenMatchInfos) {
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
}
