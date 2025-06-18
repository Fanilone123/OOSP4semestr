package controller;

import model.CalculatorModel;
import view.CalculatorView;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        while (true) {
            String expression = view.getExpression();
            
            if (expression.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                double result = model.calculate(expression);
                view.displayResult(result);
            } catch (Exception e) {
                view.displayError(e.getMessage());
            }
        }
    }
}