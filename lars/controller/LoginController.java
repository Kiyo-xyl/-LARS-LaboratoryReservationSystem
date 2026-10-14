package lars.controller;

import lars.data.LARSData;
import lars.model.Admin;
import lars.model.Student;
import lars.model.User;

/* Handles login, logout, sign up and profile editing. */
public class LoginController {

    private static final int MIN_PASSWORD_LENGTH = 6;

    private final LARSData data = LARSData.getInstance();

    /* Logs in a student using username OR Student ID. */
    public Student loginStudent(String usernameOrId, String password) {
        if (Validator.isBlank(usernameOrId) || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Please enter your username or Student ID and your password.");
        }
        String input = usernameOrId.trim();
        Student student = data.findStudentByUsername(input);
        if (student == null) {
            student = data.findStudentByStudentId(input);
        }
        // One message for both "no such user" and "wrong password"
        if (student == null || !student.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid username/Student ID or password.");
        }
        data.setCurrentUser(student);
        return student;
    }

    /** Logs in an administrator */
    public Admin loginAdmin(String username, String password) {
        if (Validator.isBlank(username) || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Please enter the administrator username and password.");
        }
        Admin admin = data.findAdminByUsername(username.trim());
        if (admin == null || !admin.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid administrator username or password.");
        }
        data.setCurrentUser(admin);
        return admin;
    }

    /** Clears the session only. Accounts, reservations and schedules stay in memory. */
    public void logout() {
        data.logout();
    }

    public User getCurrentUser() {
        return data.getCurrentUser();
    }

    /** Creates a new student account */
    public Student registerStudent(String fullName, String studentId, String course, String yearLevel,
                                   String section, String username, String password, String confirmPassword) {
        if (Validator.isBlank(fullName) || Validator.isBlank(studentId) || Validator.isBlank(course)
                || Validator.isBlank(yearLevel) || Validator.isBlank(section)
                || Validator.isBlank(username) || password == null || password.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()) {
            throw new IllegalArgumentException("Please fill in all required fields.");
        }
        if (!Validator.isValidName(fullName)) {
            throw new IllegalArgumentException(
                    "Full name may only contain letters, spaces, hyphens, apostrophes and periods.");
        }
        if (!Validator.isValidStudentId(studentId.trim())) {
            throw new IllegalArgumentException("Invalid Student ID. Use the format YYYY-NNNNN (example: 2025-07054).");
        }
        if (data.findStudentByStudentId(studentId.trim()) != null) {
            throw new IllegalArgumentException("An account with this Student ID already exists.");
        }
        if (data.isUsernameTaken(username.trim())) {
            throw new IllegalArgumentException("This username is already taken. Please choose another one.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }

        Student student = new Student(fullName.trim(), studentId.trim(), course, yearLevel,
                section.trim(), username.trim(), password);
        data.addStudent(student);
        data.fireDataChanged();
        return student;
    }

    /** Lets the logged-in student edit the allowed profile fields (not Student ID, not username). */
    public void updateProfile(String fullName, String course, String yearLevel, String section) {
        Student student = data.requireStudent();
        if (Validator.isBlank(fullName) || Validator.isBlank(course)
                || Validator.isBlank(yearLevel) || Validator.isBlank(section)) {
            throw new IllegalArgumentException("Please fill in all required fields.");
        }
        if (!Validator.isValidName(fullName)) {
            throw new IllegalArgumentException(
                    "Full name may only contain letters, spaces, hyphens, apostrophes and periods.");
        }
        student.updateProfile(fullName.trim(), course, yearLevel, section.trim());
        data.fireDataChanged();
    }
}