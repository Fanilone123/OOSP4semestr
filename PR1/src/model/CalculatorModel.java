package model;

import java.util.*;
import java.util.regex.*;

public class CalculatorModel {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(-?\\d+\\.?\\d*)|(//)|(\\+|-|\\*|/|\\^|\\(|\\))"
    );
    private static final Pattern VALID_EXPRESSION = Pattern.compile(
        "^\\s*-?\\d+(\\.\\d+)?(\\s*([+\\-*/^//])\\s*-?\\d+(\\.\\d+)?)*\\s*$"
    );

    public double calculate(String expression) throws IllegalArgumentException {
        expression = expression.replaceAll("\\s+", "");
        
        if (!VALID_EXPRESSION.matcher(expression).matches()) {
            throw new IllegalArgumentException("Invalid mathematical expression");
        }

        List<String> tokens = tokenize(expression);
        List<String> rpn = convertToRPN(tokens);
        return evaluateRPN(rpn);
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(expression);
        
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        
        return tokens;
    }

    private List<String> convertToRPN(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // Remove the '(' from stack
            } else { // It's an operator
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                       getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    private double evaluateRPN(List<String> rpn) {
        Deque<Double> stack = new ArrayDeque<>();

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                MathOperator op = MathOperator.fromSymbol(token);
                stack.push(op.apply(a, b));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return stack.pop();
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isOperator(String token) {
        return token.matches("[+\\-*/^//]");
    }

    private int getPrecedence(String operator) {
        return MathOperator.fromSymbol(operator).getPrecedence();
    }
}