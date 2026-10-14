package lars.model;

/**
 * What a computer looks like for ONE specific date and time slot.
 * operating hours, or the time has already passed.
 */
public enum AvailabilityStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    MAINTENANCE("Maintenance"),
    UNAVAILABLE("Unavailable"),
    CLOSED("Closed");

    private final String label;

    AvailabilityStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}