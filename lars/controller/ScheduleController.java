package lars.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lars.data.LARSData;
import lars.model.LabSchedule;
import lars.model.Laboratory;
import lars.model.Reservation;
import lars.model.ReservationStatus;

/** Weekly schedule management - administrators only. Saturday and Sunday can never be opened. */
public class ScheduleController {

    private final LARSData data = LARSData.getInstance();

    /** seven schedules (Monday to Sunday) of one laboratory. */
    public List<LabSchedule> getSchedules(String laboratoryName) {
        data.requireAdmin();
        return findLab(laboratoryName).getSchedules();
    }

    /* Upcoming reservations */
    public List<Reservation> findConflicts(String laboratoryName, DayOfWeek day, boolean open,
                                           LocalTime openingTime, LocalTime closingTime) {
        data.requireAdmin();
        findLab(laboratoryName);
        rejectWeekend(day);

        // Building a LabSchedule validates the times (closing must be after opening).
        LabSchedule proposed = new LabSchedule(day, openingTime, closingTime, open);

        List<Reservation> conflicts = new ArrayList<>();
        for (Reservation reservation : data.getReservations()) {
            if (reservation.getLaboratoryName().equals(laboratoryName)
                    && reservation.getDate().getDayOfWeek() == day
                    && reservation.getStatus() == ReservationStatus.UPCOMING
                    && !proposed.covers(reservation.getStartTime(), reservation.getEndTime())) {
                conflicts.add(reservation);
            }
        }
        return conflicts;
    }

    /* Applies a schedule change. */
    public int updateSchedule(String laboratoryName, DayOfWeek day, boolean open,
                              LocalTime openingTime, LocalTime closingTime, boolean cancelConflicts) {
        List<Reservation> conflicts = findConflicts(laboratoryName, day, open, openingTime, closingTime);
        if (!conflicts.isEmpty() && !cancelConflicts) {
            throw new IllegalStateException(conflicts.size()
                    + " upcoming reservation(s) conflict with this change. "
                    + "Cancel them or choose different hours.");
        }
        for (Reservation reservation : conflicts) {
            reservation.cancel();
        }
        findLab(laboratoryName).getSchedule(day).update(open, openingTime, closingTime);
        data.fireDataChanged();
        return conflicts.size();
    }

    private Laboratory findLab(String laboratoryName) {
        Laboratory lab = data.getLaboratory(laboratoryName);
        if (lab == null) {
            throw new IllegalArgumentException("Unknown laboratory: " + laboratoryName);
        }
        return lab;
    }

    private void rejectWeekend(DayOfWeek day) {
        if (LabSchedule.isWeekend(day)) {
            throw new IllegalArgumentException("Saturday and Sunday are always closed and cannot be changed.");
        }
    }
}