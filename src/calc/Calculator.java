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
    final static String MISSING_OPERATOR = "Missing operator or parentheses";
    final static String OP_NOT_FOUND = "Operator not found";


    private boolean isDigit(String token) {
//        String numbers = "1234567890";
        return "1234567890".contains(token);
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

    private void checkParentheses(ArrayList<String> expr) {
        Deque<String> openParent = new ArrayDeque<>();
        openParent.push("");
        for (String ch : expr) {
            if(ch.equals(")")) {
                if ("(".equals(openParent.peek())) {
                    openParent.pop();
                } else {
                    throw new RuntimeException(MISSING_OPERATOR);
                }
            } else if (ch.equals("(")) {
                openParent.push(ch);
            }

        }
        openParent.pop();
        if (!openParent.isEmpty()) {
            throw new RuntimeException(MISSING_OPERATOR);
        }
    }

    private void addImplicitMultiplication(ArrayList<String> expr) {
        for (int i = 1; i < expr.size()-1; i++) {
            if (expr.get(i).equals("(")
                    && (isDigit(expr.get(i-1)) || expr.get(i-1).equals(")"))) {
                expr.add(i,"*");
                i++;
            } else if (expr.get(i).equals(")")
                    && (isDigit(expr.get(i+1)))) {
                expr.add(i+1,"*");
                i++;
            }
        }
    }

    List<String> tokenize(String text) {
        List<String> unfiltered = Arrays.asList(text.split(""));
        List<String> toKeep = Arrays.asList("(",")","+","-","*","/","^","0","1","2","3","4","5","6","7","8","9");
        ArrayList<String> postRemoved = new ArrayList<>(unfiltered);
        postRemoved.retainAll(toKeep);
        checkParentheses(postRemoved);
        addImplicitMultiplication(postRemoved);
        return digitsToNumbers(postRemoved);
    }

    private void checkOperands(List<String> list) {
        List<String> operators = Arrays.asList("+","-","*","/","^");
        List<String> operatorLength = new ArrayList<>(list);
        operatorLength.retainAll(operators);
        if (operatorLength.size() * 2 + 1 != list.size()){
            throw new RuntimeException(MISSING_OPERAND);
        }
    }

    private boolean isOperator(String token) {
//        String operators = "+-*/^";
        return "+-*/^".contains(token);
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

    private Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    private int getPrecedence(String op) {
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

    private void operatorRules(int i, List<String> infix, Deque<String> rpn,
                               Deque<String> tempRightOperators, Deque<String> tempOperators) {
        if ( tempOperators.size() != 0                                                   // To avoid errors on next two rows
                && getPrecedence(tempOperators.peek()) >= getPrecedence(infix.get(i))           // Checks if the precedence is correct
                && getAssociativity(infix.get(i)) == Assoc.LEFT ) {                             // Checks if the association is correct
            for ( String j:tempRightOperators ) {                                                            // Empties result to RPN
                rpn.push(j);
                tempRightOperators.pop();
            }
            rpn.push(tempOperators.peek());                                                     // Pushes the lower precedence & Adds the higher to temp
            tempOperators.pop();
            tempOperators.push(infix.get(i));

        } else if ( tempOperators.size() != 0
                && getPrecedence(tempOperators.peek()) >= getPrecedence(infix.get(i))) {
            tempRightOperators.push(infix.get(i));
        } else if ( isOperator(infix.get(i)) ) {                                                // Pushes the operator
            tempOperators.push(infix.get(i));
        }
    }

    private void applyRules(List<String> infix, Deque<String> rpn,
                            Deque<String> tempRightOperators, Deque<String> tempOperators) {
        for ( int i = 0; i < infix.size(); i++ ) {
            if ( infix.get(i).equals(")") ) {                                                       // Removes parentheses if one is closed
                break;
            } else if ( infix.get(i).equals("(") ){                                                 // Calls itself to execute over THE parentheses
                List<String> recList = infix2Postfix( infix.subList(i+1, infix.size() ));           // Saves the result RPN
                for ( int j = 0; j < recList.size(); j++ ){                                         // Empties result to RPN
                    rpn.push(recList.get(j));
                }
                i += recList.size() - 1 + countParenthesesInNest(infix.subList(i+1,infix.size()));  // Jump to right after THE parentheses closes
            } else if ( isNumber(infix.get(i)) ) {                                                  // Pushes number
                rpn.push(infix.get(i));
            } else {
                operatorRules(i,infix,rpn,tempRightOperators,tempOperators);
            }
        }
    }

    private void emptyTemp(Deque<String> tempDeque, Deque<String> rpn) {
        for ( String i:tempDeque ) {                                                            // Empties result to RPN
            rpn.push(i);
        }
    }

    List<String> infix2Postfix(List<String> infix) {
        Deque<String> rpn = new ArrayDeque<>();
        Deque<String> tempRightOperators = new ArrayDeque<>();
        Deque<String> tempOperators = new ArrayDeque<>();
        applyRules(infix,rpn,tempRightOperators,tempOperators);
        emptyTemp(tempRightOperators,rpn);
        emptyTemp(tempOperators,rpn);
        List<String> finished = new ArrayList<>(rpn);                                               // Reverses the stack
        Collections.reverse(finished);
        return finished;
    }

    private int countParenthesesInNest(List<String> list) {                                                 // Returns the amount of times you need to skip to get out of the current nest
        int count = 1;                                                                              // amount of parentheses
        int starts = 1;                                                                             // amount of start parentheses compared to end parentheses
        int index = 0;                                                                              // index for the while loop
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

    private double applyOperator(String op, double d1, double d2) {
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

    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> postfix = infix2Postfix(tokenize(expr));
        checkOperands(postfix);
        int currentIndex = 0;
        while (currentIndex < postfix.size()) {
            if ( isOperator(postfix.get(currentIndex)) ) {
                double d1 = Double.parseDouble(postfix.get(currentIndex-1));
                double d2 = Double.parseDouble(postfix.get(currentIndex-2));
                String newValue = Double.toString(applyOperator(postfix.get(currentIndex), d1, d2));
                postfix.remove(currentIndex-2);
                postfix.remove(currentIndex-2);
                postfix.remove(currentIndex-2);
                postfix.add(currentIndex-2,newValue);
                currentIndex -= 2;
            }
            currentIndex ++;
        }
        return Double.parseDouble(postfix.get(0)); // 0 just for now, should be: return evalPostfix(postfix);
    }

}
