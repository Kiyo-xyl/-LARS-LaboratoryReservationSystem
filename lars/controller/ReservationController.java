package lars.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import lars.data.LARSData;
import lars.model.AvailabilityStatus;
import lars.model.Computer;
import lars.model.ComputerStatus;
import lars.model.LabSchedule;
import lars.model.Laboratory;
import lars.model.Reservation;
import lars.model.ReservationStatus;
import lars.model.Student;

/* All STUDENT reservation logic. */
public class ReservationController {

    /** Time dropdowns */
    public static final int TIME_STEP_MINUTES = 30;

    private final LARSData data = LARSData.getInstance();

    // ---------------------------------------------------------------- time options for dropdowns

    /**
     * Times from opening to closing for the lab on that date, for the start/end dropdowns.
     * Returns an empty list when the lab is closed (weekend, or a day the admin closed).
     */
    public List<LocalTime> getTimeSlots(String laboratoryName, LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();
        Laboratory lab = data.getLaboratory(laboratoryName);
        if (lab == null || date == null) {
            return slots;
        }
        LabSchedule schedule = lab.getSchedule(date.getDayOfWeek());
        if (schedule == null || !schedule.isOpen()) {
            return slots;
        }
        LocalTime time = schedule.getOpeningTime();
        while (!time.isAfter(schedule.getClosingTime())) {
            slots.add(time);
            LocalTime next = time.plusMinutes(TIME_STEP_MINUTES);
            if (next.isBefore(time)) {
                break; // passed midnight
            }
            time = next;
        }
        return slots;
    }

    // ---------------------------------------------------------------- availability (for computer cards)

    /* How does this computer look for the chosen date and time slot? */
    public AvailabilityStatus getAvailability(Computer computer, LocalDate date, LocalTime start, LocalTime end) {
        if (computer.getStatus() == ComputerStatus.MAINTENANCE) {
            return AvailabilityStatus.MAINTENANCE;
        }
        if (computer.getStatus() == ComputerStatus.UNAVAILABLE) {
            return AvailabilityStatus.UNAVAILABLE;
        }
        if (date == null || start == null || end == null
                || findTimeProblem(computer.getLaboratoryName(), date, start, end) != null) {
            return AvailabilityStatus.CLOSED;
        }
        if (data.findConflict(computer, date, start, end) != null) {
            return AvailabilityStatus.RESERVED;
        }
        return AvailabilityStatus.AVAILABLE;
    }

    // ---------------------------------------------------------------- creating a reservation

    public Reservation createReservation(Computer computer, LocalDate date, LocalTime start, LocalTime end) {
        Student student = data.requireStudent();
        validate(student, computer, date, start, end);

        Reservation reservation = new Reservation(data.nextReservationId(), student, computer, date, start, end);
        data.addReservation(reservation);
        data.fireDataChanged();
        return reservation;
    }

    /** Runs every rule from the spec */
    private void validate(Student student, Computer computer, LocalDate date, LocalTime start, LocalTime end) {
        if (computer == null) {
            throw new IllegalArgumentException("Please select a computer.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Please select a reservation date.");
        }
        if (start == null || end == null) {
            // On a weekend or a closed day the time lists are empty.
            String closedProblem = findClosedDayProblem(computer.getLaboratoryName(), date);
            if (closedProblem != null) {
                throw new IllegalArgumentException(closedProblem);
            }
            throw new IllegalArgumentException("Please select both a start time and an end time.");
        }
        if (!Validator.isValidName(student.getFullName())) {
            throw new IllegalArgumentException("Your account name is invalid. Please correct it in My Profile.");
        }
        if (!Validator.isValidStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("Invalid Student ID. Use the format YYYY-NNNNN.");
        }

        String timeProblem = findTimeProblem(computer.getLaboratoryName(), date, start, end);
        if (timeProblem != null) {
            throw new IllegalArgumentException(timeProblem);
        }

        String label = computer.getLaboratoryName() + " " + computer.getComputerNumber();
        if (computer.getStatus() == ComputerStatus.MAINTENANCE) {
            throw new IllegalArgumentException(label + " is under maintenance and cannot be reserved.");
        }
        if (computer.getStatus() == ComputerStatus.UNAVAILABLE) {
            throw new IllegalArgumentException(label + " is currently unavailable and cannot be reserved.");
        }
        if (data.findConflict(computer, date, start, end) != null) {
            throw new IllegalArgumentException(label + " is already reserved for that time. "
                    + "Please choose another time or another computer.");
        }
    }

    /* Checks date/time rules only. */
    private String findTimeProblem(String laboratoryName, LocalDate date, LocalTime start, LocalTime end) {
        if (!end.isAfter(start)) {
            return "The end time must be later than the start time.";
        }
        if (date.isBefore(LocalDate.now())) {
            return "You cannot reserve a computer for a past date.";
        }

        // Weekend / closed-day check comes BEFORE the "time already passed" check so the user gets the clearest message.
        String closedProblem = findClosedDayProblem(laboratoryName, date);
        if (closedProblem != null) {
            return closedProblem;
        }
        if (date.equals(LocalDate.now()) && start.isBefore(LocalTime.now())) {
            return "The start time has already passed. Please choose a later start time.";
        }

        DayOfWeek day = date.getDayOfWeek();
        LabSchedule schedule = data.getLaboratory(laboratoryName).getSchedule(day);
        if (!schedule.covers(start, end)) {
            String dayName = day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            return laboratoryName + " operating hours on " + dayName + " are "
                    + Validator.TIME_FORMAT.format(schedule.getOpeningTime()) + " to "
                    + Validator.TIME_FORMAT.format(schedule.getClosingTime())
                    + ". Please stay within these hours.";
        }
        return null;
    }

    /* Returns a message if the laboratory is closed on this date */
    private String findClosedDayProblem(String laboratoryName, LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        if (LabSchedule.isWeekend(day)) {
            return "Reservations are not allowed on Saturdays and Sundays. The laboratories are closed on weekends.";
        }
        Laboratory lab = data.getLaboratory(laboratoryName);
        if (lab == null) {
            return "Unknown laboratory.";
        }
        LabSchedule schedule = lab.getSchedule(day);
        if (schedule == null || !schedule.isOpen()) {
            return laboratoryName + " is closed on " + day.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ".";
        }
        return null;
    }

    // ---------------------------------------------------------------- the student's own reservations

    /** Only the logged-in student's reservations, sorted by date then start time. */
    public List<Reservation> getMyReservations() {
        Student student = data.requireStudent();
        List<Reservation> mine = new ArrayList<>();
        for (Reservation reservation : data.getReservations()) {
            if (reservation.getStudent().getStudentId().equals(student.getStudentId())) {
                mine.add(reservation);
            }
        }
        mine.sort(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime));
        return mine;
    }

    /** Number of this student's reservations that are not cancelled (for the dashboard). */
    public int countMyReservations() {
        int count = 0;
        for (Reservation reservation : getMyReservations()) {
            if (reservation.isActive()) {
                count++;
            }
        }
        return count;
    }

    /** Cancels one of the logged-in student's own reservations. */
    public void cancelMyReservation(Reservation reservation) {
        Student student = data.requireStudent();
        if (reservation == null) {
            throw new IllegalArgumentException("Please select a reservation first.");
        }
        if (!reservation.getStudent().getStudentId().equals(student.getStudentId())) {
            throw new SecurityException("You can only cancel your own reservations.");
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Completed reservations cannot be cancelled.");
        }
        reservation.cancel(); // throws if it was already cancelled
        data.fireDataChanged();
    }
}