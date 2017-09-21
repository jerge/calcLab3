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
        boolean parenthesis = false;
        for ( int i = 0; i < infix.size(); i++ ) {
            if ( infix.get(i).equals("(")) {
                
            } else if ( infix.get(i).equals(")")){

//            if ( parenthesis && infix.get(i).equals(")") ) {   // Removes parenthesis if one is closed
//                tempOperators.pop();
//                parenthesis = false;
//            } else if ( parenthesis ){              //
//                for (String thing:( infix2Postfix(infix.subList(i,infix.size())) )) {
//                    rpn.push(thing);
//                    // CHECK IF REVErSED
//                }
            } else if ( isNumber(infix.get(i)) ) {     // Remember that all numbers are only 1 character
                rpn.push(infix.get(i));
            } else if ( tempOperators.size() != 0
                    && getPrecedence(tempOperators.peek()) >= getPrecedence(infix.get(i))
                    && getAssociativity(infix.get(i)) == Assoc.LEFT ) {
                rpn.push(tempOperators.peek());
                tempOperators.pop();
                tempOperators.push(infix.get(i));
            } else if ( isOperator(infix.get(i)) ) {
                tempOperators.push(infix.get(i));
//            } else {                        // Always a parenthesis
//                parenthesis = true;
//                tempOperators.push(infix.get(i));
            }
        }
        for ( String i:tempOperators ) {
            rpn.push(i);
            tempOperators.pop();
        }
        List<String> finished = new ArrayList<>(rpn);
        Collections.reverse(finished);
        return finished;
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
//        String asd = "asd20+8asd1 23 * asd25 -asd 2äad";
//        List<String> tokens = tokenize(asd);    //   <---------------- HERE are the methods!!!!
//        out.println(tokenize(asd));
//        List<String> postfix = infix2Postfix(tokens);
//        out.println("Hand-parsed:");
//        out.println("20 25 8123 * 2 - +");
//        out.println("Computer-parsed:");
//        out.println(postfix);
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
        out.println(infix2Postfix(tokenize("(1+2)*3")).toString()          .equals(new ArrayList<String>(Arrays.asList("1 2 + 3 *".split(" "))).toString()) );
//        out.println(infix2Postfix(tokenize("2^(1+1)")).toString()          .equals(new ArrayList<String>(Arrays.asList("2 1 1 + ^".split(" "))).toString()) );

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
