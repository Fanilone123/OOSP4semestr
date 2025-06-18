package model;

import java.util.function.DoubleBinaryOperator;

public enum MathOperator {
    ADD("+", 1, (a, b) -> a + b),
    SUBTRACT("-", 1, (a, b) -> a - b),
    MULTIPLY("*", 2, (a, b) -> a * b),
    DIVIDE("/", 2, (a, b) -> {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    }),
    POWER("^", 3, Math::pow),
    POWER2("**", 3, Math::pow),
    INT_DIV("//", 2, (a, b) -> Math.floor(a / b));

    private final String symbol;
    private final int precedence;
    private final DoubleBinaryOperator operation;

    MathOperator(String symbol, int precedence, DoubleBinaryOperator operation) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.operation = operation;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public double apply(double a, double b) {
        return operation.applyAsDouble(a, b);
    }

    public static MathOperator fromSymbol(String symbol) {
        for (MathOperator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}