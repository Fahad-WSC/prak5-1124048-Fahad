package src.models;

public abstract class SmartDevice {
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

    public abstract String getDeviceDetails();
}