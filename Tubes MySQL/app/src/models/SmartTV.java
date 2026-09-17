package src.models;

public class SmartTV extends SmartDevice implements Switchable, Connectable {
    private int channel;
    private int volume;
    private ConnectionType connectionType;

    public SmartTV(String id, String nama, double daya, int channel, int volume, ConnectionType connectionType) {
        super(id, nama, daya);
        this.channel = channel;
        this.volume = volume;
        this.connectionType = connectionType;
        turnOn();
    }

    public int getChannel() { return channel; }
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
        return String.format("• Smart TV [%s] (ID: %s) - Daya: %.1fW | Status: %s | Koneksi: %s | Channel: %d | Volume: %d",
                getNama(), getId(), getDaya(), getStatus(), connectionType, channel, volume);
    }
}