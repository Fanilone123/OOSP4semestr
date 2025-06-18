package view;

import java.util.Scanner;

public class CalculatorView {
    private Scanner scanner = new Scanner(System.in);

    public String getExpression() {
        System.out.println("Введите математическое выражение (или 'exit' для выхода):");
        System.out.println("Поддерживаемые операции: +, -, *, /, ^, **, //, !");
        System.out.println("Функции: log(), exp()");
        System.out.print("> ");
        return scanner.nextLine();
    }

    public void displayResult(double result) {
        System.out.printf("Результат: %.6f%n", result);
    }

    public void displayError(String message) {
        System.out.println("Ошибка: " + message);
    }
}