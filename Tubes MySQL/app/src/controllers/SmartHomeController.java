package src.controllers;

import models.*;
import views.SmartHomeView;
import util.InputUtil;
import java.util.ArrayList;
import java.util.List;

public class SmartHomeController {
    private List<SmartDevice> devices;
    private SmartHomeView view;

    public SmartHomeController() {
        this.devices = new ArrayList<>();
        this.view = new SmartHomeView();
    }

    public void run() {
        boolean running = true;
        while (running) {
            int choice = view.showMainMenu();
            switch (choice) {
                case 1:
                    handleAddDevice();
                    break;
                case 2:
                    view.displayDevices(devices);
                    break;
                case 3:
                    running = false;
                    view.showMessage("Keluar dari sistem.");
                    break;
                default:
                    view.showMessage("Pilihan tidak valid!");
            }
        }
    }

    private void handleAddDevice() {
        int typeChoice = view.showDeviceTypeMenu();

        if (typeChoice < 1 || typeChoice > 3) {
            view.showMessage("Jenis perangkat tidak valid!");
            return;
        }

        System.out.print("ID: ");
        String id = InputUtil.readLine();
        System.out.print("Nama: ");
        String nama = InputUtil.readLine();
        System.out.print("Daya (W): ");
        double daya = InputUtil.readDouble();

        switch (typeChoice) {
            case 1:
                System.out.print("Channel: ");
                int channel = InputUtil.readInt();
                System.out.print("Volume: ");
                int tvVolume = InputUtil.readInt();
                System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH): ");
                int tvConnChoice = InputUtil.readInt();
                ConnectionType tvConn = (tvConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                devices.add(new SmartTV(id, nama, daya, channel, tvVolume, tvConn));
                view.showMessage("Smart TV berhasil ditambahkan!");
                break;

            case 2:
                System.out.print("Volume: ");
                int spVolume = InputUtil.readInt();
                System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH): ");
                int spConnChoice = InputUtil.readInt();
                ConnectionType spConn = (spConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                devices.add(new SmartSpeaker(id, nama, daya, spVolume, spConn));
                view.showMessage("Smart Speaker berhasil ditambahkan!");
                break;

            case 3:
                System.out.print("PIN: ");
                String pin = InputUtil.readLine();

                devices.add(new SmartDoorLock(id, nama, daya, pin));
                view.showMessage("Smart Door Lock berhasil ditambahkan!");
                break;
        }
    }
}