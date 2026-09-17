package src;
import controllers.SmartHomeController;

public class App {
    public static void main(String[] args) {
        SmartHomeController controller = new SmartHomeController();
        controller.run();
    }
}