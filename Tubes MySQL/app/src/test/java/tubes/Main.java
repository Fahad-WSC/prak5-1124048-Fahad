package pbo.fahad;

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

    public String getNama() {
        return nama;
    }

    public double getDaya() {
        return daya;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Abstract Method wajib sesuai soal
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
        turnOn(); // Atur status awal menyala
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
        turnOn(); // Atur status awal menyala
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
        lock(); // Atur status awal terkunci
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
class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtils() {}

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka desimal/bulat!");
            }
        }
    }
}

// --- Main Class ---
public class Main {
    private static final List<SmartDevice> daftarDevice = new ArrayList<>();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Tambah Perangkat");
            System.out.println("2. Print Semua Perangkat");
            System.out.println("3. Keluar");
            int pilihan = InputUtils.getInt("Pilih opsi (1-3): ");

            switch (pilihan) {
                case 1:
                    tambah();
                    break;
                case 2:
                    printSemua();
                    break;
                case 3:
                    running = false;
                    System.out.println("Keluar dari program.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void tambah() {
        System.out.println("\n--- Tambah Perangkat ---");
        System.out.println("1. Smart TV");
        System.out.println("2. Smart Speaker");
        System.out.println("3. Smart Door Lock");

        int pilih = InputUtils.getInt("Pilih (1-3): ");

        switch (pilih) {
            case 1:
                String tvId = InputUtils.getString("ID: ");
                String tvNama = InputUtils.getString("Nama: ");
                double tvDaya = InputUtils.getDouble("Daya (W): ");
                int tvChannel = InputUtils.getInt("Channel: ");
                int tvVolume = InputUtils.getInt("Volume: ");
                System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH): ");
                int tvConnChoice = InputUtils.getInt("Pilihan: ");
                ConnectionType tvConn = (tvConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                daftarDevice.add(new SmartTV(tvId, tvNama, tvDaya, tvChannel, tvVolume, tvConn));
                System.out.println("Smart TV berhasil ditambahkan!");
                break;

            case 2:
                String spId = InputUtils.getString("ID: ");
                String spNama = InputUtils.getString("Nama: ");
                double spDaya = InputUtils.getDouble("Daya (W): ");
                int spVolume = InputUtils.getInt("Volume: ");
                System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH): ");
                int spConnChoice = InputUtils.getInt("Pilihan: ");
                ConnectionType spConn = (spConnChoice == 2) ? ConnectionType.BLUETOOTH : ConnectionType.WIFI;

                daftarDevice.add(new SmartSpeaker(spId, spNama, spDaya, spVolume, spConn));
                System.out.println("Smart Speaker berhasil ditambahkan!");
                break;

            case 3:
                String dlId = InputUtils.getString("ID: ");
                String dlNama = InputUtils.getString("Nama: ");
                double dlDaya = InputUtils.getDouble("Daya (W): ");
                String pin = InputUtils.getString("PIN: ");

                daftarDevice.add(new SmartDoorLock(dlId, dlNama, dlDaya, pin));
                System.out.println("Smart Door Lock berhasil ditambahkan!");
                break;

            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    private static void printSemua() {
        System.out.println("\n--- Daftar Semua Perangkat ---");
        if (daftarDevice.isEmpty()) {
            System.out.println("Belum ada perangkat terdaftar.");
            return;
        }

        // Penerapan Polymorphism
        for (SmartDevice device : daftarDevice) {
            System.out.println(device.getDeviceDetails());
        }
    }
}