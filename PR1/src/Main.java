import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public classMain {
    public static void main(String[] args) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView();
        CalculatorController controller = new CalculatorController(model, view);
        
        System.out.println("Калькулятор математических выражений");
        System.out.println("Поддерживаемые операции: +, -, *, /, ^, //");
        System.out.println("Пример ввода: -3234+843/3234-4232123/(34+123+32+5)*3234");
        
        controller.run();
    }
}
