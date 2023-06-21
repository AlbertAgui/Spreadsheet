package org.example;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.ContentException;
import org.example.Formula.Parsing;

import java.util.LinkedList;
import java.util.Stack;

public class GeneratePostfix {
    private static Integer get_precedence(String token) throws ContentException {
        if (Parsing.is_highp_operator(token)) return 2;
        if (Parsing.is_lowp_operator(token)) return 1;
        if (Parsing.isColon(token)) return 0;
        throw new ContentException("Get precedence: precedence not found");
    }

    public static LinkedList<String> generate_postfix(LinkedList<String> tokens) throws ContentException {
        LinkedList<String> postfix = new LinkedList<>();
        Stack<String> aux_stack = new Stack<>();
        for (int i = 0; i < tokens.size(); ++i) {
            String next = tokens.get(i);
            if(Parsing.is_number(next) || Parsing.is_cell_id(next)) {
                postfix.add(next);
            } else if (Parsing.is_operator(next)){
                while (!aux_stack.isEmpty() && (Parsing.is_operator(aux_stack.peek()) &&
                        (get_precedence(next) <= get_precedence(aux_stack.peek())) || Parsing.is_function(aux_stack.peek()))) { //pop and out until top has lower precendence than next
                    postfix.add(aux_stack.pop());
                }
                aux_stack.push(next);
            } else if(Parsing.is_function(next)) {
                aux_stack.push(next);
            } else if(Parsing.is_open_claw(next)) {
                aux_stack.push(next);
            } else if (Parsing.is_closed_claw(next)) { //close clause: pop and out until top is '('
                while (!aux_stack.isEmpty() && !Parsing.is_open_claw(aux_stack.peek())) { //equals or match??
                    postfix.add(aux_stack.pop());
                }
                aux_stack.pop(); // pop "(", not add ")", See, no checked stack size
            } else if(Parsing.isSemicolon(next)) {
                while (!aux_stack.isEmpty() && !Parsing.is_open_claw(aux_stack.peek())) {
                    postfix.add(aux_stack.pop());
                }
            } else if (Parsing.isColon(next)) {
                while (!aux_stack.isEmpty() && (Parsing.is_operator(aux_stack.peek()) &&
                        (get_precedence(next) <= get_precedence(aux_stack.peek())))) { //pop and out until top has lower precendence than next
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


    public static void DisplayPostfix(LinkedList<String> postfix){
        System.out.println("postfix count:" + postfix.size());
        postfix.forEach(pf ->{
            System.out.println(pf);
        });
    }
}
