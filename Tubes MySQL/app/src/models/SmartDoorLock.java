package src.models;

public class SmartDoorLock extends SmartDevice implements Lockable {
    private String pin;

    public SmartDoorLock(String id, String nama, double daya, String pin) {
        super(id, nama, daya);
        this.pin = pin;
        lock();
    }

    public String getPin() { return pin; }

    @Override
    public void lock() { setStatus("Terkunci"); }

    @Override
    public void unlock() { setStatus("Terbuka"); }

    @Override
   public String getDeviceDetails() {
    return "Smart Door Lock [" + getNama() + "] (ID: " + getId()
            + ") - Daya: " + getDaya() + "W | Status: " + getStatus()
            + " | PIN: ****";
}
}