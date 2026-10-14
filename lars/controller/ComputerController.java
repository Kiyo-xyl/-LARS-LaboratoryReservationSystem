package lars.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lars.data.LARSData;
import lars.model.AvailabilityStatus;
import lars.model.Computer;
import lars.model.ComputerStatus;
import lars.model.Laboratory;
import lars.model.Reservation;
import lars.model.ReservationStatus;

/** Computer lists, "right now" counts for dashboards, and admin status changes. */
public class ComputerController {

    private final LARSData data = LARSData.getInstance();

    /** The 20 computers of one laboratory */
    public List<Computer> getComputersOf(String laboratoryName) {
        Laboratory lab = data.getLaboratory(laboratoryName);
        if (lab == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(lab.getComputers());
    }

    /** All 40 computers - administrators only. */ 
    public List<Computer> getAllComputers() {
        data.requireAdmin();
        return data.getAllComputers();
    }

    /* The status of a computer RIGHT NOW */
    public AvailabilityStatus getStatusNow(Computer computer) {
        if (computer.getStatus() == ComputerStatus.MAINTENANCE) {
            return AvailabilityStatus.MAINTENANCE;
        }
        if (computer.getStatus() == ComputerStatus.UNAVAILABLE) {
            return AvailabilityStatus.UNAVAILABLE;
        }
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        for (Reservation reservation : data.getReservations()) {
            if (reservation.isActive()
                    && reservation.getComputer().getUniqueId().equals(computer.getUniqueId())
                    && reservation.getDate().equals(today)
                    && !now.isBefore(reservation.getStartTime())
                    && now.isBefore(reservation.getEndTime())) {
                return AvailabilityStatus.RESERVED;
            }
        }
        return AvailabilityStatus.AVAILABLE;
    }

    public int countTotal(String laboratoryName) {
        return computersOf(laboratoryName).size();
    }

    /* Counts computers whose current status */
    public int countNow(String laboratoryName, AvailabilityStatus status) {
        int count = 0;
        for (Computer computer : computersOf(laboratoryName)) {
            if (getStatusNow(computer) == status) {
                count++;
            }
        }
        return count;
    }

    private List<Computer> computersOf(String laboratoryName) {
        if (laboratoryName == null) {
            return data.getAllComputers();
        }
        return getComputersOf(laboratoryName);
    }

    // ---------------------------------------------------------------- admin actions

    /** How many upcoming reservations this computer has. The screen uses it to warn the admin. */
    public int countUpcomingReservations(Computer computer) {
        data.requireAdmin();
        int count = 0;
        for (Reservation reservation : data.getReservations()) {
            if (reservation.getComputer().getUniqueId().equals(computer.getUniqueId())
                    && reservation.getStatus() == ReservationStatus.UPCOMING) {
                count++;
            }
        }
        return count;
    }

    /* Changes the administrative status. */
    public int setComputerStatus(Computer computer, ComputerStatus newStatus) {
        data.requireAdmin();
        if (computer == null || newStatus == null) {
            throw new IllegalArgumentException("Please select a computer and a status.");
        }
        int affected = 0;
        if (newStatus != ComputerStatus.AVAILABLE) {
            affected = countUpcomingReservations(computer);
        }
        computer.setStatus(newStatus);
        data.fireDataChanged();
        return affected;
    }
}