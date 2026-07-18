package tubes.model;

public enum PaymentMethod {
    CASH("Tunai"),
    DEBIT("Kartu Debit/Kredit"),
    QRIS("QRIS");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
