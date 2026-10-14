package lars.controller;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lars.model.Student;

/** text validation and the date/time formats used in every screen. */
public final class Validator {

    /** Shows times like */
    public static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    /** Shows dates like */
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

    private Validator() {
        // utility class
    }

    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    /** Letters (any language), spaces, hyphens, apostrophes and periods. Digits are rejected. */
    public static boolean isValidName(String name) {
        return name != null && name.trim().matches("[\\p{L}][\\p{L} .'\\-]*");
    }

    /** YYYY-NNNNN, */
    public static boolean isValidStudentId(String studentId) {
        return Student.isValidStudentId(studentId);
    }
}