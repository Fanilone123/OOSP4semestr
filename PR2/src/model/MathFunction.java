package model;

import java.util.function.DoubleUnaryOperator;

public enum MathFunction {
    FACTORIAL("!", x -> {
        if (x < 0) throw new IllegalArgumentException("Factorial of negative number");
        double result = 1;
        for (int i = 2; i <= x; i++) {
            result *= i;
        }
        return result;
    }),
    LOG("log", x -> Math.log(x) / Math.log(2)),
    EXP("exp", Math::exp);

    private final String symbol;
    private final DoubleUnaryOperator operation;

    MathFunction(String symbol, DoubleUnaryOperator operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    public String getSymbol() {
        return symbol;
    }

    public double apply(double x) {
        return operation.applyAsDouble(x);
    }

    public static MathFunction fromSymbol(String symbol) {
        for (MathFunction func : values()) {
            if (func.symbol.equals(symbol)) {
                return func;
            }
        }
        throw new IllegalArgumentException("Unknown function: " + symbol);
    }
}