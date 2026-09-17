package src.views;

import models.SmartDevice;
import util.InputUtil;
import java.util.List;

public class SmartHomeView {

    public int showMainMenu() {
        System.out.println("\n=== SMART HOME SYSTEM ===");
        System.out.println("1. Tambah Perangkat");
        System.out.println("2. Print Semua Perangkat");
        System.out.println("3. Keluar");
        System.out.print("Pilihan: ");
        return InputUtil.readInt();
    }

    public int showDeviceTypeMenu() {
        System.out.println("\n--- Pilih Jenis Perangkat ---");
        System.out.println("1. Smart TV");
        System.out.println("2. Smart Speaker");
        System.out.println("3. Smart Door Lock");
        System.out.print("Pilihan: ");
        return InputUtil.readInt();
    }

    public void displayDevices(List<SmartDevice> devices) {
        System.out.println("\n--- Daftar Semua Perangkat ---");
        if (devices.isEmpty()) {
            System.out.println("Belum ada perangkat terdaftar.");
            return;
        }
        for (SmartDevice device : devices) {
            System.out.println(device.getDeviceDetails());
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}