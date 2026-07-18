package tubes.model;

public enum Status {
    PAID, FINISHED;

    public String getLabel() {
        switch (this) {
            case PAID:
                return "Sudah Dibayar";
            case FINISHED:
                return "Selesai";
            default:
                return this.name();
        }
    }
}
