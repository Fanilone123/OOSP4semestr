package view;

import java.util.Scanner;

public class CalculatorView {
    private Scanner scanner = new Scanner(System.in);

    public String getExpression() {
        System.out.print("Введите математическое выражение (или 'exit' для выхода): ");
        return scanner.nextLine();
    }

    public void displayResult(double result) {
        System.out.printf("Результат: %.4f%n", result);
    }

    public void displayError(String message) {
        System.out.println("Ошибка: " + message);
    }
}