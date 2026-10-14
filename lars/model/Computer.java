package lars.model;

/* One physical computer. The unique id combines lab + number */
public class Computer {

    private final String uniqueId;
    private final String laboratoryName;
    private final String computerNumber; // e.g. "PC01"
    private ComputerStatus status;

    public Computer(String laboratoryName, String computerNumber) {
        this.laboratoryName = laboratoryName;
        this.computerNumber = computerNumber;
        this.uniqueId = laboratoryName + "-" + computerNumber;
        this.status = ComputerStatus.AVAILABLE;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getLaboratoryName() {
        return laboratoryName;
    }

    public String getComputerNumber() {
        return computerNumber;
    }

    public ComputerStatus getStatus() {
        return status;
    }

    public void setStatus(ComputerStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    /** True when the administrator allows reservations for this computer. */
    public boolean isBookable() {
        return status == ComputerStatus.AVAILABLE;
    }
}