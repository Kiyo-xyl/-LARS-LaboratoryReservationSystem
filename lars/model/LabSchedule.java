package lars.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * The opening hours of ONE laboratory on ONE weekday.
 * Saturday and Sunday are always closed and cannot be opened.
 */
public class LabSchedule {

    private final DayOfWeek day;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean open;

    public LabSchedule(DayOfWeek day, LocalTime openingTime, LocalTime closingTime, boolean open) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.open = false; // start closed, then let update() apply the rules
        update(open, openingTime, closingTime);
    }

    public static boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /** Changes the schedule after checking the rules. Throws IllegalArgumentException if invalid. */
    public void update(boolean newOpen, LocalTime newOpening, LocalTime newClosing) {
        if (isWeekend(day)) {
            if (newOpen) {
                throw new IllegalArgumentException("The laboratory must stay closed on Saturday and Sunday.");
            }
            this.open = false;
            return;
        }
        if (newOpening == null || newClosing == null) {
            throw new IllegalArgumentException("Opening and closing times are required.");
        }
        if (!newClosing.isAfter(newOpening)) {
            throw new IllegalArgumentException("Closing time must be later than opening time.");
        }
        this.open = newOpen;
        this.openingTime = newOpening;
        this.closingTime = newClosing;
    }

    /** True if the lab is open and the whole time range fits inside opening hours. */
    public boolean covers(LocalTime start, LocalTime end) {
        return open && !start.isBefore(openingTime) && !end.isAfter(closingTime);
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public boolean isOpen() {
        return open;
    }
}