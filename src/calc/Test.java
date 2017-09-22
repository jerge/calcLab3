package calc;

import java.util.*;

import static java.lang.System.out;

/**
 * This is a test program for the Calculator
 * It should output true for everything
 *
 * Right click and run ...
 */
public class Test {

    public static void main(String[] args) {
        new Test().test();
    }

    final Calculator calculator = new Calculator();

    final static String MISSING_OPERAND = "Missing or bad operand";

    private List<String> tokenize(String text) {
        List<String> unfiltered = Arrays.asList(text.split(""));         // TODO LOOK AT TRIM
        ArrayList<String> toBeRemoved = new ArrayList<>();
        for (String token:unfiltered) {
            if ( !isOperator(token) && !isDigit(token) && !isParenthesis(token) ) {  // TODO Remember to make sure parenthesis succeeds
                toBeRemoved.add(token);
            }
        }
        ArrayList<String> postRemoved = new ArrayList<>(unfiltered);
        postRemoved.removeAll(toBeRemoved);
        checkParentheses(postRemoved);
        return digitsToNumbers(postRemoved);
    }

    private List<String> digitsToNumbers( List<String> unNumberized ) {
        Deque<String> filtered = new ArrayDeque<>();
        StringBuilder temp = new StringBuilder();
        for ( String token:unNumberized ) {
            if ( isDigit(token) ) {
                temp.append(token);
            } else if (temp.toString().equals("")) {
                filtered.push(token);
            } else {
                filtered.push(temp.toString());
                filtered.push(token);
                temp.setLength(0);              // Clears temp
            }
        }
        if (!temp.toString().equals("")) {
            filtered.push(temp.toString());
        }
        List<String> finished = new ArrayList<>(filtered);
        Collections.reverse(finished);
        return finished;
    }

    private boolean isOperator(String token) {
//        String operators = "+-*/^";
        return "+-*/^".contains(token);
    }

    private boolean isParenthesis(String token) {
        return "()".contains(token);
    }

    private boolean isDigit(String token) {
//        String numbers = "1234567890";
        return "1234567890".contains(token);
    }

    private boolean isNumber(String text) {
        String[] split = text.split("");
        for(String ch: split) {
            if(!isDigit(ch)){
                return false;
            }
        }
        return true;
    }

    enum Assoc {
        LEFT,
        RIGHT
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

    boolean checkParentheses(ArrayList<String> expr) {
        Deque<String> grabb = new ArrayDeque<>();
        grabb.push("");
        for (String ch : expr) {
            if(ch.equals(")")) {
                if ("(".equals(grabb.peek())) {
                    grabb.pop();
                } else {
                    throw new RuntimeException(MISSING_OPERAND);
                }
            } else if (ch.equals("(")) {
                grabb.push(ch);
            }

        }
        grabb.pop();
        return grabb.isEmpty();
    }

    // This is interesting because have to return, but what if no match?!?

    // TODO FIX IS NUMBER

    final static String OP_NOT_FOUND = "Operator not found";


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

    private List<String> infix2Postfix(List<String> infix) {
        Deque<String> rpn = new ArrayDeque<>();
        Deque<String> tempOperators = new ArrayDeque<>();
        for ( int i = 0; i < infix.size(); i++ ) {
            if ( infix.get(i).equals(")") ) {           // Removes parenthesis if one is closed
            break;
            } else if ( infix.get(i).equals("(") ){     // Calls itself to execute over THE parenthesis
                List<String> recList = infix2Postfix( infix.subList(i+1, infix.size() ));   // Saves the result RPN
                for ( int j = 0; j < recList.size(); j++ ){     // Empties result to RPN
                   rpn.push(recList.get(j));
                }
                i += recList.size() - 1 + countParenthesisInNest(infix.subList(i+1,infix.size()));  // Jump to right after THE parenthesis closes
            } else if ( isNumber(infix.get(i)) ) {              // Pushes number
                rpn.push(infix.get(i));
            } else if ( tempOperators.size() != 0               // To avoid errors on next two rows
                    && getPrecedence(tempOperators.peek()) >= getPrecedence(infix.get(i))   // Checks if the precedence is correct
                    && getAssociativity(infix.get(i)) == Assoc.LEFT ) {                     // Checks if the association is correct
                rpn.push(tempOperators.peek());     // Pushes the lower precedence & Adds the higher to temp
                tempOperators.pop();
                tempOperators.push(infix.get(i));
            } else if ( isOperator(infix.get(i)) ) {    // Pushes the operator
                tempOperators.push(infix.get(i));
            } else {
                throw new RuntimeException("Det blev fel");
            }
        }
        for ( String i:tempOperators ) {    // Empties result to RPN
            rpn.push(i);
            tempOperators.pop();
        }
        List<String> finished = new ArrayList<>(rpn);       // Reverses the stack
        Collections.reverse(finished);
        return finished;
    }

    private int countParenthesisInNest(List<String> list) { // Returns the amount of times you need to skip to get out of the current nest
        int count = 1;
        int starts = 1;
        int index = 0;
        while (starts > 0) {
            if (list.get(index).equals("(")) {
                count ++;
                starts ++;
            } else if (list.get(index).equals(")")) {
                count ++;
                starts --;
            }
            index ++;
        }
        return count;
    }


    void test() {

        // Here you could write your own test for "small" helper methods



        // inp: 20+8123*25-2
        // res: 20 25 8123 * 2 - +
        // steps: rpn[] tempOperators[] input[]
        // [20] [] [+ 8123 * 25 - 2] push 20
        // [20] [+] [8123 * 25 - 2] pushT op
        // [8123 20] [+] [* 25 - 2] push 8123
        // [8123 20] [* +] [25 - 2] pushT op (due to higher precedence)
        // [25 8123 20] [* +] [- 2] push 25
        // [* 25 8123 20] [- +] [2] pushT op (due to current < precedence)
        // [2 * 25 8123 20] [- +] push 2
        // [+ - 2 * 25 8123 20] empty T
        // [20 8123 25 * 2 - +] reverse

        List<String> ex1 = Arrays.asList("1+(2+3))*(((3+5)))".split(""));
        out.println(countParenthesisInNest(ex1) == 4);

        String asd = "(1*(2+3))*(3+5)";
        // 1 2 3 + * 3 5 + *
        List<String> tokens = tokenize(asd);    //   <---------------- HERE are the methods!!!!
        out.println(tokenize(asd));
        List<String> postfix = infix2Postfix(tokens);
//        out.println("Hand-parsed:");
//        out.println("20 25 8123 * 2 - +");
        out.println("Computer-parsed:");
        out.println(postfix);
//        out.println(tokenize(asd));
        /*
        out.println( call some helper method );

        */

        // Uncomment below line by line to test (also must uncomment helper methods, last)

        // Tokenization ---------------------------

//        out.println(tokenize("1 + 10"));   // Arguments are input and expected output
//        out.println(tokenize("1+ 10"));  // Expected is in fact a list [ "1", "+", "10"]
//        out.println(tokenize("1 +10"));
//        out.println(tokenize("1+10"));
//        out.println(tokenize("(1+10) "));
//        out.println(tokenize("2 *( 1+10) "));
//        out.println(tokenize("(1 +2) /2 *( 1+10) "));



        // Infix to postfix -----------------------

//        new ArrayList<String>(Arrays.asList("1 10 +".split(" ")));
//        out.println(tokenize("4 3 ^ 2 *"));
//        out.println(infix2Postfix(tokenize("1+10")).toString());
//        out.println(new ArrayList<String>(Arrays.asList("1 10 +".split(" "))).toString());
        out.println(infix2Postfix(tokenize("1+10")).toString()             .equals(new ArrayList<>(Arrays.asList("1 10 +".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("1+2+3")).toString()            .equals(new ArrayList<>(Arrays.asList("1 2 + 3 +".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("1+2-3")).toString()            .equals(new ArrayList<>(Arrays.asList("1 2 + 3 -".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("3-2-1")).toString()            .equals(new ArrayList<>(Arrays.asList("3 2 - 1 -".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("1 + 2 * 3")).toString()        .equals(new ArrayList<>(Arrays.asList( "1 2 3 * +".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("1 / 2 + 3")).toString()        .equals(new ArrayList<>(Arrays.asList("1 2 / 3 +".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("20/4/2")).toString()           .equals(new ArrayList<>(Arrays.asList("20 4 / 2 /".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("4^3^2")).toString()            .equals(new ArrayList<>(Arrays.asList("4 3 2 ^ ^".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("4^3*2")).toString()            .equals(new ArrayList<>(Arrays.asList("4 3 ^ 2 *".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("(1+2)*3")).toString()          .equals(new ArrayList<>(Arrays.asList("1 2 + 3 *".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("2^(1+1)")).toString()          .equals(new ArrayList<>(Arrays.asList("2 1 1 + ^".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("(1*(2+3))*3")).toString()      .equals(new ArrayList<>(Arrays.asList("1 2 3 + * 3 *".split(" "))).toString()) );
        out.println(infix2Postfix(tokenize("2^(1+1)")).toString()          .equals(new ArrayList<>(Arrays.asList("2 1 1 + ^".split(" "))).toString()) );

        // Evaluation ------------------------------
       /*
        // A value
        e("123", 123);

        // Basic operations
        e("1 + 10", 11);
        e("1 + 0", 1);
        e("1 - 10", -9);  // Input may not be negative but output may
        e("10 - 1", 9);
        e("60 * 10", 600);
        e("60 * 0", 0);
        e("3 / 2", 1.5);  // See exception for div by zero
        e("1 / 2", 0.5);
        e("2 ^ 4 ", 16);
        e("2 ^ 0 ", 1);

        // Associativity
        e("10 - 5 - 2", 3);  // (10-5)-2
        e("20 / 2 / 2", 5);  // (20/2)/2
        e("4 ^ 2 ^ 2", 256);  // 4^(2^2)

        // Precedence
        e("3 * 10 + 2", 32);
        e("3 + 10 * 2", 23);
        e("30 / 3 + 2", 12);
        e("1 + 30 / 3", 11);
        e("3 * 2 ^ 2", 12);
        e("3 ^ 2 * 2", 18);

        // Parentheses
        e("10 - (5 - 2)", 7);
        e("20 / (10 / 2)", 4);
        e("(3 ^ 2) ^ 2", 81);
        e("3 * (10 + 2)", 36);
        e("30 / (3 + 2)", 6);
        e("(3 + 2) ^ 2", 25);
        e(" 2 ^ (1 + 1)", 4);
        e(" ((((1 + 1))) * 2)", 4);

        // Mix priority and right and left associativity
        e(" 1 ^ 1 ^ 1 ^ 1  - 1", 0);
        e(" 4 - 2 - 1 ^ 2 ", 1);

        // Exceptions -----------------------------------
        try {
            e("1 / 0 ", 0);   // 0 just a dummy
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage().equals(Calculator.DIV_BY_ZERO));
        }
        try {
            e("1 + 2 + ", 0);
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage().equals(Calculator.MISSING_OPERAND));
        }
        try {
            e("12 3", 0);
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage().equals(Calculator.MISSING_OPERATOR));
        }
        try {
            e("1 + 2)", 0);
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage().equals(Calculator.MISSING_OPERATOR));
        }
        */
    }

    // -----   Below are helper test methods, don't bother ...  --------------
    /*
    // t for tokenize, a very short name, lazy, avoid typing ...
    void t(String expr, String expected) {
        List<String> list = calculator.tokenize(expr);
        String result = String.join(" ", list);
        out.println(result.equals(expected));
    }
    */
    /*
    // Infix 2 postfix
    void i2p(String infix, String expected) {
        List<String> tokens = calculator.tokenize(infix);
        List<String> postfix = calculator.infix2Postfix(tokens);
        String result = String.join(" ", postfix);
        out.println(result.equals(expected));
    }
    */
    /*
    // Evaluation
    void e(String infix, double expected) {
        List<String> tokens = calculator.tokenize(infix);
        List<String> postfix = calculator.infix2Postfix(tokens);
        double result = calculator.evalPostfix(postfix);
        out.println(result == expected);
    }*/

}
