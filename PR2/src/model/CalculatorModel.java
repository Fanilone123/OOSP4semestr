package model;

import java.util.*;
import java.util.regex.*;

public class CalculatorModel {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(-?\\d+\\.?\\d*)|(//|\\*\\*|[+\\-*/^!])|(log|exp)|([()])"
    );
    private static final Pattern VALID_EXPRESSION = Pattern.compile(
        "^\\s*-?\\d+(\\.\\d+)?(\\s*([+\\-*/^!]|//|\\*\\*)\\s*-?\\d+(\\.\\d+)?)*\\s*$"
    );
    private static final int MAX_TERMS = 15;

    public double calculate(String expression) throws IllegalArgumentException {
        expression = expression.replaceAll("\\s+", "");
        
        validateExpression(expression);

        List<String> tokens = tokenize(expression);
        List<String> rpn = convertToRPN(tokens);
        return evaluateRPN(rpn);
    }

    private void validateExpression(String expression) {
        // Проверка баланса скобок
        if (!checkParenthesesBalance(expression)) {
            throw new IllegalArgumentException("Unbalanced parentheses");
        }

        // Проверка количества операторов
        long operatorCount = expression.chars()
            .filter(c -> "+-*/^!".indexOf(c) != -1)
            .count();
        
        if (operatorCount >= MAX_TERMS) {
            throw new IllegalArgumentException("Too many terms (max " + MAX_TERMS + ")");
        }
    }

    private boolean checkParenthesesBalance(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(expression);
        
        while (matcher.find()) {
            String token = matcher.group();
            if (token != null && !token.isEmpty()) {
                tokens.add(token);
            }
        }
        
        return tokens;
    }

    private List<String> convertToRPN(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isFunction(token)) {
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // Remove the '(' from stack
                if (!stack.isEmpty() && isFunction(stack.peek())) {
                    output.add(stack.pop());
                }
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
            } else if (isFunction(token)) {
                double x = stack.pop();
                MathFunction func = MathFunction.fromSymbol(token);
                stack.push(func.apply(x));
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                MathOperator op = MathOperator.fromSymbol(token);
                stack.push(op.apply(a, b));
            } else if (token.equals("!")) {
                double x = stack.pop();
                stack.push(MathFunction.FACTORIAL.apply(x));
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
        return token.matches("[+\\-*/^]|//|\\*\\*");
    }

    private boolean isFunction(String token) {
        return token.matches("log|exp");
    }

    private int getPrecedence(String operator) {
        if (operator.equals("!")) {
            return 4;
        }
        try {
            return MathOperator.fromSymbol(operator).getPrecedence();
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}