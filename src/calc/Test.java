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

    void e(String expr, double answer){
        out.println(calculator.eval(expr) == answer);
    }

    void t(String expr, String answer){
        out.println(calculator.tokenize(expr).toString().equals(answer));
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

//        List<String> ex1 = Arrays.asList("1+(2+3))*(((3+5)))".split(""));
//        out.println(calculator.countParenthesesInNest(ex1) == 4);
//
//        String asd = " (1 + 2) ^ 3  - 4";
//        // 1 2 3 + * 3 5 + *
//        List<String> tokens = calculator.tokenize(asd);    //   <---------------- HERE are the methods!!!!
//        out.println(calculator.tokenize(asd));
//        List<String> postfix = calculator.infix2Postfix(tokens);
//        out.println("Computer-parsed:");
//        out.println(postfix);

        // Uncomment below line by line to test (also must uncomment helper methods, last)

        // Tokenization ---------------------------


        out.println(calculator.tokenize("1 + 10"));   // Arguments are input and expected output
        out.println(calculator.tokenize("1+ 10"));  // Expected is in fact a list [ "1", "+", "10"]
        out.println(calculator.tokenize("(2)(2)"));

        out.println(calculator.tokenize("1+10"));
        out.println(calculator.tokenize("(1+10) "));
        out.println(calculator.tokenize("2 *( 1+10) "));
        out.println(calculator.tokenize("(1 +2) /2 *( 1+10) "));

        out.println(calculator.infix2Postfix(calculator.tokenize("4^3^2")));
        out.println(calculator.infix2Postfix(calculator.tokenize(" 1 ^ 2 ^ 3 ^ 4  - 5")));

        // Infix to postfix -----------------------

        out.println("Infix to postfix");
        out.println(calculator.infix2Postfix(calculator.tokenize("1+10")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 10 +".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("1+2+3")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 2 + 3 +".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("1+2-3")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 2 + 3 -".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("3-2-1")).toString()
                                .equals(new ArrayList<>(Arrays.asList("3 2 - 1 -".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("1 + 2 * 3")).toString()
                                .equals(new ArrayList<>(Arrays.asList( "1 2 3 * +".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("1 / 2 + 3")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 2 / 3 +".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("20/4/2")).toString()
                                .equals(new ArrayList<>(Arrays.asList("20 4 / 2 /".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("4^3^2")).toString()
                                .equals(new ArrayList<>(Arrays.asList("4 3 2 ^ ^".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("4^3*2")).toString()
                                .equals(new ArrayList<>(Arrays.asList("4 3 ^ 2 *".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("(1+2)*3")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 2 + 3 *".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("2^(1+1)")).toString()
                                .equals(new ArrayList<>(Arrays.asList("2 1 1 + ^".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("(1*(2+3))*3")).toString()
                                .equals(new ArrayList<>(Arrays.asList("1 2 3 + * 3 *".split(" "))).toString()) );

        out.println(calculator.infix2Postfix(calculator.tokenize("2^(1+1)")).toString()
                                .equals(new ArrayList<>(Arrays.asList("2 1 1 + ^".split(" "))).toString()) );


        // Evaluation ------------------------------

        // A value
        e("123", 123);

        out.println("Basic operations");
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

        out.println("Associativity");
        // Associativity
        e("10 - 5 - 2", 3);  // (10-5)-2
        e("20 / 2 / 2", 5);  // (20/2)/2
        e("4 ^ 2 ^ 2", 256);  // 4^(2^2)

        out.println("Precedence");
        // Precedence
        e("3 * 10 + 2", 32);
        e("3 + 10 * 2", 23);
        e("30 / 3 + 2", 12);
        e("1 + 30 / 3", 11);
        e("3 * 2 ^ 2", 12);
        e("3 ^ 2 * 2", 18);

        out.println("Parentheses");
        // Parentheses
        e("10 - (5 - 2)", 7);
        e("20 / (10 / 2)", 4);
        e("(3 ^ 2) ^ 2", 81);
        e("3 * (10 + 2)", 36);
        e("30 / (3 + 2)", 6);
        e("(3 + 2) ^ 2", 25);
        e(" 2 ^ (1 + 1)", 4);
        e(" ((((1 + 1))) * 2)", 4);

        out.println("Mix priority and right and left associativity");
        // Mix priority and right and left associativity
        e(" 1 ^ 1 ^ 1 ^ 1  - 1", 0);
        e(" 4 - 2 - 1 ^ 2 ", 1);

        // Exceptions -----------------------------------
//        e("1 / 0 ", 0);     // DIV BY ZERO
//        e("1 + 2 + ", 0);   // MISSING OPERAND
//        e("1 + 2)", 0);     // MISSING OPERATOR

//        try {
//            e("1 / 0 ", 0);   // 0 just a dummy
//        } catch (IllegalArgumentException e) {
//            out.println(e.getMessage().equals(Calculator.DIV_BY_ZERO));
//        }
//        try {
//            e("1 + 2 + ", 0);
//        } catch (IllegalArgumentException e) {
//            out.println(e.getMessage().equals(Calculator.MISSING_OPERAND));
//        }
//        try {
//            e("12 3", 0);
//        } catch (IllegalArgumentException e) {
//            out.println(e.getMessage().equals(Calculator.MISSING_OPERATOR));
//        }
//        try {
//            e("1 + 2)", 0);
//        } catch (IllegalArgumentException e) {
//            out.println(e.getMessage().equals(Calculator.MISSING_OPERATOR));
//        }

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
