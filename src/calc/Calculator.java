package calc;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
public class Calculator {

    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";

    // Definition of operators
    final String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);    //   <---------------- HERE are the methods!!!!
        List<String> postfix = infix2Postfix(tokens);
        return 0; // 0 just for now, should be: return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

   // TODO
    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    // Error messages
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // TODO

    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    // ---------- Tokenize -----------------------
    // TODO



    private List<String> tokenize(String text) {
        List<String> unfiltered = Arrays.asList(text.split(""));         // TODO LOOK AT TRIM
        ArrayList<String> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < unfiltered.size(); i++) {
            if ( !isOperator(unfiltered.get(i)) && !isDigit(unfiltered.get(i)) ) {
                toBeRemoved.add(unfiltered.get(i));
//                unfiltered.remove(i);
            }
        }
        ArrayList<String> postRemoved = new ArrayList<>(unfiltered);
        postRemoved.removeAll(toBeRemoved);
        return digitsToNumbers(postRemoved);
    }

    private List<String> digitsToNumbers( List<String> unNumberized ) {
        Deque<String> filtered = new ArrayDeque<>();
        StringBuilder temp = new StringBuilder();
        for ( String token:unNumberized ) {
            if ( isDigit(token) ) {
                temp.append(token);
            } else {
                filtered.push(temp.toString());
                filtered.push(token);
                temp.setLength(0);              // Clears temp
            }
        }
        filtered.push(temp.toString());

        List<String> finished = new ArrayList<>(filtered);
        Collections.reverse(finished);
        return finished;
    }

    private boolean isOperator(String token) {
//        String operators = "+-*/^";
        return "+-*/^()".contains(token);
    }

    private boolean isDigit(String token) {
//        String numbers = "1234567890";
        return "1234567890".contains(token);
    }

    private int readNumber(List<String> li, String text) {
        String[] split = text.split("");
        for(String ch: split) {
            if(isDigit(ch)){
                li.add(ch);
            }
        }
        return li.size();
    }

    private List<String> infix2Postfix(List<String> infix) {
        Deque<String> rpn = new ArrayDeque<>();
        Deque<String> tempOperators = new ArrayDeque<>();
        boolean parenthesis = false;
        for ( int i = 0; i < infix.size(); i++ ) {
            if ( parenthesis && infix.get(i).equals(")") ) {   // Removes parenthesis if one is closed
                tempOperators.pop();
                parenthesis = false;
            } else if ( parenthesis ){              //
                for (String thing:( infix2Postfix(infix.subList(i,infix.size())) )) {
                    rpn.push(thing);
                    // CHECK IF REVErSED
                }
            } else if ( getPrecedence(tempOperators.peek()) == getPrecedence(infix.get(i)) ){
                rpn.push(tempOperators.peek());
                tempOperators.pop();
                tempOperators.push(infix.get(i));
            } else if ( isOperator(infix.get(i)) ) {
                tempOperators.push(infix.get(i));
            } else if ( isDigit(infix.get(i)) ) {     // Remember that all numbers are only 1 character
                rpn.push(infix.get(i));
            } else {                        // Always a parenthesis
                parenthesis = true;
                tempOperators.push(infix.get(i));
            }
        }
        for ( String i:tempOperators ) {
            rpn.push(i);
            tempOperators.pop();
        }
//        List<String> finished = new ArrayList<>(rpn);
        return new ArrayList<>(rpn);
    }

//    while there are tokens to be read:
//    read a token.
//	if the token is a number, then push it to the output queue.
//	if the token is an operator, then:
//            while there is an operator at the top of the operator stack with
//    greater than or equal to precedence and the operator is left associative:
//    pop operators from the operator stack, onto the output queue.
//    push the read operator onto the operator stack.
//            if the token is a left bracket (i.e. "("), then:
//    push it onto the operator stack.
//            if the token is a right bracket (i.e. ")"), then:
//            while the operator at the top of the operator stack is not a left bracket:
//    pop operators from the operator stack onto the output queue.
//    pop the left bracket from the stack.
//		/* if the stack runs out without finding a left bracket, then there are
//		mismatched parentheses. */
//if there are no more tokens to read:
//            while there are still operator tokens on the stack:
//    /* if the operator token on the top of the stack is a bracket, then
//    there are mismatched parentheses. */
//    pop the operator onto the output queue.
//            exit.


}
