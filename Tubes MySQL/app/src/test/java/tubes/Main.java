package tubes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- Enum ---
enum ConnectionType {
    WIFI,
    BLUETOOTH,
    DISCONNECTED
}

// --- Interfaces ---
interface Switchable {
    void turnOn();
    void turnOff();
}

interface Lockable {
    void lock();
    void unlock();
}

interface Connectable {
    void connect(ConnectionType connectionType);
    void disconnect();
}

// --- Abstract Class ---
abstract class SmartDevice {
    private String id;
    private String nama;
    private double daya;
    private String status;

    public SmartDevice(String id, String nama, double daya) {
        this.id = id;
        this.nama = nama;
        this.daya = daya;
        this.status = "Mati";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getDaya() {
        return daya;
    }

    public void setDaya(double daya) {
        this.daya = daya;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract String getDeviceDetails();
}

// --- Concrete Classes ---
class SmartTV extends SmartDevice implements Switchable, Connectable {
    private int channel;
    private int volume;
    private ConnectionType connectionType;

    public SmartTV(String id, String nama, double daya, int channel, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.channel = channel;
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn(); // Mengatur status awal
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public void turnOn() {
        setStatus("Menyala");
    }

    @Override
    public void turnOff() {
        setStatus("Mati");
    }

    @Override
    public void connect(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public void disconnect() {
        this.connectionType = ConnectionType.DISCONNECTED;
    }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart TV [%s] (ID: %s) - Daya: %.1fW | Status: %s | Koneksi: %s | Channel: %d | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, channel, volume);
    }
}

class SmartSpeaker extends SmartDevice implements Switchable, Connectable {
    private int volume;
    private ConnectionType connectionType;

    public SmartSpeaker(String id, String nama, double daya, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn(); // Mengatur status awal
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public void turnOn() {
        setStatus("Menyala");
    }

    @Override
    public void turnOff() {
        setStatus("Mati");
    }

    @Override
    public void connect(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public void disconnect() {
        this.connectionType = ConnectionType.DISCONNECTED;
    }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart Speaker [%s] (ID: %s) - Daya: %.1fW | Status: %s | Koneksi: %s | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, volume);
    }
}

class SmartDoorLock extends SmartDevice implements Lockable {
    private String pin;

    public SmartDoorLock(String id, String nama, double daya, String pin) {
        super(id, nama, daya);
        this.pin = pin;
        lock(); // Mengatur status awal
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public void lock() {
        setStatus("Terkunci");
    }

    @Override
    public void unlock() {
        setStatus("Terbuka");
    }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart Door Lock [%s] (ID: %s) - Daya: %.1fW | Status: %s | PIN: ****",
                getNama(), getId(), getDaya(), getStatus());
    }
}

// --- Static Utility Class ---
class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}

// --- Main Class ---
public class Main {
    public static void main(String[] args) {
        List<SmartDevice> deviceList = new ArrayList<>();
        boolean running = true;

        while (running) {
            System.out.println("\n=== SMART HOME SYSTEM ===");
            System.out.println("1. Tambah Perangkat");
            System.out.println("2. Print Semua Perangkat");
            System.out.println("3. Keluar");
            int choice = InputUtil.readInt("Pilihan: ");

            switch (choice) {
                case 1:
                    tambahPerangkatMenu(deviceList);
                    break;
                case 2:
                    printSemuaPerangkat(deviceList);
                    break;
                case 3:
                    running = false;
                    System.out.println("Keluar dari sistem.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void tambahPerangkatMenu(List<SmartDevice> deviceList) {
        System.out.println("\n--- Tambah Perangkat ---");
        System.out.println("1. Smart TV");
        System.out.println("2. Smart Speaker");
        System.out.println("3. Smart Door Lock");
        int subChoice = InputUtil.readInt("Pilihan: ");

        switch (subChoice) {
            case 1:
                String tvId = InputUtil.readString("Masukkan ID: ");
                String tvNama = InputUtil.readString("Masukkan Nama: ");
                double tvDaya = InputUtil.readDouble("Masukkan Daya (W): ");
                int channel = InputUtil.readInt("Masukkan Channel: ");
                int tvVolume = InputUtil.readInt("Masukkan Volume: ");
                System.out.println("Pilih Jenis Koneksi:");
                System.out.println("1. WIFI");
                System.out.println("2. BLUETOOTH");
                int tvConnChoice = InputUtil.readInt("Pilihan Koneksi: ");
                ConnectionType tvConn = (tvConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                deviceList.add(new SmartTV(tvId, tvNama, tvDaya, channel, tvVolume, tvConn));
                System.out.println("Smart TV berhasil ditambahkan!");
                break;

            case 2:
                String spId = InputUtil.readString("Masukkan ID: ");
                String spNama = InputUtil.readString("Masukkan Nama: ");
                double spDaya = InputUtil.readDouble("Masukkan Daya (W): ");
                int spVolume = InputUtil.readInt("Masukkan Volume: ");
                System.out.println("Pilih Jenis Koneksi:");
                System.out.println("1. WIFI");
                System.out.println("2. BLUETOOTH");
                int spConnChoice = InputUtil.readInt("Pilihan Koneksi: ");
                ConnectionType spConn = (spConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                deviceList.add(new SmartSpeaker(spId, spNama, spDaya, spVolume, spConn));
                System.out.println("Smart Speaker berhasil ditambahkan!");
                break;

            case 3:
                String dlId = InputUtil.readString("Masukkan ID: ");
                String dlNama = InputUtil.readString("Masukkan Nama: ");
                double dlDaya = InputUtil.readDouble("Masukkan Daya (W): ");
                String pin = InputUtil.readString("Masukkan PIN: ");

                deviceList.add(new SmartDoorLock(dlId, dlNama, dlDaya, pin));
                System.out.println("Smart Door Lock berhasil ditambahkan!");
                break;

            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    private static void printSemuaPerangkat(List<SmartDevice> deviceList) {
        System.out.println("\n--- Daftar Semua Perangkat ---");
        if (deviceList.isEmpty()) {
            System.out.println("Belum ada perangkat yang terdaftar.");
            return;
        }

        // Penerapan Polymorphism
        for (SmartDevice device : deviceList) {
            System.out.println(device.getDeviceDetails());
        }
    }
}