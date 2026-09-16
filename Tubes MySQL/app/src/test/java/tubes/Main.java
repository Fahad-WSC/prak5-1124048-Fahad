import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- Enum ---
enum ConnectionType {
    WIFI, BLUETOOTH, NONE
}

// --- Interfaces ---
interface Connectable {
    void connect(ConnectionType type);
    void disconnect();
}

interface Switchable {
    void turnOn();
    void turnOff();
}

interface Lockable {
    void lock();
    void unlock();
}

// --- Abstract Class ---
abstract class SmartDevice {
    private String id;
    private String nama;
    private int daya;
    private String status;

    public SmartDevice(String id, String nama, int daya) {
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

    public int getDaya() {
        return daya;
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

    public SmartTV(String id, String nama, int daya, int channel, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.channel = channel;
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn(); // Mengatur status awal
    }

    public int getChannel() {
        return channel;
    }

    public int getVolume() {
        return volume;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
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
    public void connect(ConnectionType type) {
        this.connectionType = type;
    }

    @Override
    public void disconnect() {
        this.connectionType = ConnectionType.NONE;
    }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart TV [%s] (ID: %s) - Daya: %dW | Status: %s | Koneksi: %s | Channel: %d | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, channel, volume);
    }
}

class SmartSpeaker extends SmartDevice implements Switchable, Connectable {
    private int volume;
    private ConnectionType connectionType;

    public SmartSpeaker(String id, String nama, int daya, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn(); // Mengatur status awal
    }

    public int getVolume() {
        return volume;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
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
    public void connect(ConnectionType type) {
        this.connectionType = type;
    }

    @Override
    public void disconnect() {
        this.connectionType = ConnectionType.NONE;
    }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart Speaker [%s] (ID: %s) - Daya: %dW | Status: %s | Koneksi: %s | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, volume);
    }
}

class SmartDoorLock extends SmartDevice implements Lockable {
    private String pin;

    public SmartDoorLock(String id, String nama, int daya, String pin) {
        super(id, nama, daya);
        this.pin = pin;
        lock(); // Mengatur status awal
    }

    public String getPin() {
        return pin;
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
        return String.format("• Smart Door Lock [%s] (ID: %s) - Daya: %dW | Status: %s | PIN: ****",
                getNama(), getId(), getDaya(), getStatus());
    }
}

// --- Static Utility Class ---
class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static int readInt() {
        int val = scanner.nextInt();
        scanner.nextLine(); // Membersihkan karakter newline agar readLine() tidak terlewati
        return val;
    }

    public static String readLine() {
        return scanner.nextLine();
    }
}

// --- Main Class ---
public class Main {
    public static void main(String[] args) {
        List<SmartDevice> devices = new ArrayList<>();
        int input = -1;

        while (input != 0) {
            System.out.println("\n=== SMART HOME SYSTEM ===");
            System.out.println("1. Tambah Perangkat");
            System.out.println("2. Print Semua Perangkat");
            System.out.println("0. Keluar");
            System.out.print("Pilihan: ");
            input = InputUtil.readInt();

            if (input == 1) {
                System.out.println("\n--- Pilih Jenis Perangkat ---");
                System.out.println("1. Smart TV");
                System.out.println("2. Smart Speaker");
                System.out.println("3. Smart Door Lock");
                System.out.print("Pilihan: ");
                int jenis = InputUtil.readInt();

                if (jenis == 1) {
                    System.out.print("ID: ");
                    String id = InputUtil.readLine();
                    System.out.print("Nama: ");
                    String nama = InputUtil.readLine();
                    System.out.print("Daya (W): ");
                    int daya = InputUtil.readInt();
                    System.out.print("Channel: ");
                    int channel = InputUtil.readInt();
                    System.out.print("Volume: ");
                    int volume = InputUtil.readInt();
                    System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH / 3. NONE): ");
                    int connChoice = InputUtil.readInt();
                    ConnectionType conn = ConnectionType.NONE;
                    if (connChoice == 1) conn = ConnectionType.WIFI;
                    else if (connChoice == 2) conn = ConnectionType.BLUETOOTH;

                    devices.add(new SmartTV(id, nama, daya, channel, volume, conn));
                    System.out.println("Smart TV berhasil ditambahkan!");

                } else if (jenis == 2) {
                    System.out.print("ID: ");
                    String id = InputUtil.readLine();
                    System.out.print("Nama: ");
                    String nama = InputUtil.readLine();
                    System.out.print("Daya (W): ");
                    int daya = InputUtil.readInt();
                    System.out.print("Volume: ");
                    int volume = InputUtil.readInt();
                    System.out.println("Pilih Koneksi (1. WIFI / 2. BLUETOOTH / 3. NONE): ");
                    int connChoice = InputUtil.readInt();
                    ConnectionType conn = ConnectionType.NONE;
                    if (connChoice == 1) conn = ConnectionType.WIFI;
                    else if (connChoice == 2) conn = ConnectionType.BLUETOOTH;

                    devices.add(new SmartSpeaker(id, nama, daya, volume, conn));
                    System.out.println("Smart Speaker berhasil ditambahkan!");

                } else if (jenis == 3) {
                    System.out.print("ID: ");
                    String id = InputUtil.readLine();
                    System.out.print("Nama: ");
                    String nama = InputUtil.readLine();
                    System.out.print("Daya (W): ");
                    int daya = InputUtil.readInt();
                    System.out.print("PIN: ");
                    String pin = InputUtil.readLine();

                    devices.add(new SmartDoorLock(id, nama, daya, pin));
                    System.out.println("Smart Door Lock berhasil ditambahkan!");
                }

            } else if (input == 2) {
                System.out.println("\n--- Daftar Perangkat ---");
                if (devices.isEmpty()) {
                    System.out.println("Belum ada perangkat terdaftar.");
                } else {
                    for (SmartDevice device : devices) {
                        System.out.println(device.getDeviceDetails());
                    }
                }
            } else if (input == 0) {
                System.out.println("Keluar dari program.");
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }
}