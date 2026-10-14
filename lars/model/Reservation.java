package lars.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * One computer reservation. Cancelling keeps the record (for history)
 * but it no longer blocks the computer.
 */
public class Reservation {

    private final String reservationId;
    private final Student student;
    private final Computer computer;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private boolean cancelled;

    public Reservation(String reservationId, Student student, Computer computer,
                       LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (student == null || computer == null || date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("All reservation details are required.");
        }
        this.reservationId = reservationId;
        this.student = student;
        this.computer = computer;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Student getStudent() {
        return student;
    }

    public Computer getComputer() {
        return computer;
    }

    public String getLaboratoryName() {
        return computer.getLaboratoryName();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    /** A reservation is active until it is cancelled. */
    public boolean isActive() {
        return !cancelled;
    }

    public void cancel() {
        if (cancelled) {
            throw new IllegalStateException("This reservation is already cancelled.");
        }
        cancelled = true;
    }

    /** Status is calculated from the real date and time, so it is never out of date. */
    public ReservationStatus getStatus() {
        if (cancelled) {
            return ReservationStatus.CANCELLED;
        }
        if (LocalDateTime.of(date, endTime).isBefore(LocalDateTime.now())) {
            return ReservationStatus.COMPLETED;
        }
        return ReservationStatus.UPCOMING;
    }

    public boolean overlaps(Computer otherComputer, LocalDate otherDate,
                            LocalTime otherStart, LocalTime otherEnd) {
        return isActive()
                && computer.getUniqueId().equals(otherComputer.getUniqueId())
                && date.equals(otherDate)
                && otherStart.isBefore(endTime)
                && otherEnd.isAfter(startTime);
    }
}