package lars.model;

/* The administrative status of a computer.*/
public enum ComputerStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    MAINTENANCE("Maintenance");

    private final String label;

    ComputerStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label; // so JComboBox and JTable show a friendly name
    }
}