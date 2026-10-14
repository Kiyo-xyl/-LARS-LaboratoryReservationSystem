package lars.model;

/** A student account. The student ID and username can never be changed after sign up. */
public class Student extends User {

    private final String studentId;
    private String course;
    private String yearLevel;
    private String section;

    public Student(String fullName, String studentId, String course, String yearLevel,
                   String section, String username, String password) {
        super(fullName, username, password, Role.STUDENT);
        if (!isValidStudentId(studentId)) {
            throw new IllegalArgumentException("Student ID must follow the format YYYY-NNNNN.");
        }
        this.studentId = studentId;
        this.course = course;
        this.yearLevel = yearLevel;
        this.section = section;
    }

    /** Format check for IDs such as 2025-07054. Used by the controllers too. */
    public static boolean isValidStudentId(String id) {
        return id != null && id.matches("\\d{4}-\\d{5}");
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourse() {
        return course;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getSection() {
        return section;
    }

    /** Only these fields may be edited from the profile page. */
    public void updateProfile(String fullName, String course, String yearLevel, String section) {
        setFullName(fullName); // validates the name
        this.course = course;
        this.yearLevel = yearLevel;
        this.section = section;
    }
}