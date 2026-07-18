package tubes.controller;

public class AdminController {

    private final MenuController menuCtrl;
    private final OrderController orderCtrl;

    public AdminController() {
        this.menuCtrl = new MenuController();
        this.orderCtrl = new OrderController();
    }

    public int getTotalMenu() {
        return menuCtrl.getTotalMenu();
    }

    public int getTotalOrder() {
        return orderCtrl.getTotalOrder();
    }

    public long getTotalPendapatan() {
        return orderCtrl.getTotalPendapatan();
    }
}
