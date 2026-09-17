package src.models;

public class SmartSpeaker extends SmartDevice implements Switchable, Connectable {
    private int volume;
    private ConnectionType connectionType;

    public SmartSpeaker(String id, String nama, double daya, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn();
    }

    public int getVolume() { return volume; }
    public ConnectionType getConnectionType() { return connectionType; }

    @Override
    public void turnOn() { setStatus("Menyala"); }

    @Override
    public void turnOff() { setStatus("Mati"); }

    @Override
    public void connect(ConnectionType type) { this.connectionType = type; }

    @Override
    public void disconnect() { this.connectionType = ConnectionType.DISCONNECTED; }

    @Override
    public String getDeviceDetails() {
        return String.format("• Smart Speaker [%s] (ID: %s) - Daya: %.1fW | Status: %s | Koneksi: %s | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, volume);
    }
}