package src.models;

public interface Connectable {
    void connect(ConnectionType type);
    void disconnect();
}