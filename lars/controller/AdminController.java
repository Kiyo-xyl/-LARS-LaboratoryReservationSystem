package lars.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lars.data.LARSData;
import lars.model.Reservation;
import lars.model.ReservationStatus;

/** Reservation management and reservation statistics - administrators only. */
public class AdminController {

    private final LARSData data = LARSData.getInstance();

    /** Every reservation from both laboratories*/
    public List<Reservation> getAllReservations() {
        data.requireAdmin();
        List<Reservation> all = new ArrayList<>(data.getReservations());
        all.sort(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime).reversed());
        return all;
    }

    /* Filters reservations.*/
    public List<Reservation> searchReservations(String studentName, String studentId, String laboratoryName,
                                                LocalDate date, ReservationStatus status) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation reservation : getAllReservations()) {
            if (!Validator.isBlank(studentName)
                    && !reservation.getStudent().getFullName().toLowerCase()
                            .contains(studentName.trim().toLowerCase())) {
                continue;
            }
            if (!Validator.isBlank(studentId)
                    && !reservation.getStudent().getStudentId().contains(studentId.trim())) {
                continue;
            }
            if (!Validator.isBlank(laboratoryName) && !laboratoryName.equals("All")
                    && !reservation.getLaboratoryName().equals(laboratoryName)) {
                continue;
            }
            if (date != null && !reservation.getDate().equals(date)) {
                continue;
            }
            if (status != null && reservation.getStatus() != status) {
                continue;
            }
            results.add(reservation);
        }
        return results;
    }

    /* Cancels any reservation */
    public void cancelReservation(Reservation reservation) {
        data.requireAdmin();
        if (reservation == null) {
            throw new IllegalArgumentException("Please select a reservation first.");
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Completed reservations cannot be cancelled.");
        }
        reservation.cancel(); // throws if it was already cancelled
        data.fireDataChanged();
    }

    /** All reservation records, including cancelled and completed ones. */
    public int countAllReservations() {
        data.requireAdmin();
        return data.getReservations().size();
    }

    /** Upcoming reservations.*/
    public int countUpcomingReservations(String laboratoryName) {
        data.requireAdmin();
        int count = 0;
        for (Reservation reservation : data.getReservations()) {
            if (reservation.getStatus() == ReservationStatus.UPCOMING
                    && (laboratoryName == null || reservation.getLaboratoryName().equals(laboratoryName))) {
                count++;
            }
        }
        return count;
    }
}