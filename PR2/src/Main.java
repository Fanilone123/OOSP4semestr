import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public class Main {
    public static void main(String[] args) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView();
        CalculatorController controller = new CalculatorController(model, view);
        
        System.out.println("Усовершенствованный калькулятор математических выражений");
        System.out.println("Пример ввода: -3234+((exp(2)*843/log(3234)-4232123)/(34+123+32+5))*3234");
        
        controller.run();
    }
}