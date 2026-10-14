package lars.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lars.model.Admin;
import lars.model.Computer;
import lars.model.Laboratory;
import lars.model.Reservation;
import lars.model.Student;
import lars.model.User;

public class LARSData {

    private static LARSData instance;

    private final List<Student> students = new ArrayList<>();
    private final List<Admin> admins = new ArrayList<>();
    private final List<Laboratory> laboratories = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private final List<Runnable> changeListeners = new ArrayList<>();

    private User currentUser;      // the logged-in user 
    private int reservationCounter = 0;

    private LARSData() {
        DataInitializer.loadDefaults(this); // runs exactly once, when the first screen asks for data
    }

    public static synchronized LARSData getInstance() {
        if (instance == null) {
            instance = new LARSData();
        }
        return instance;
    }

    // ---------------------------------------------------------------- students & admins

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public Student findStudentByUsername(String username) {
        for (Student student : students) {
            if (student.getUsername().equalsIgnoreCase(username)) {
                return student;
            }
        }
        return null;
    }

    public Student findStudentByStudentId(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    /** Package-private: only DataInitializer may add admins. */
    void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public Admin findAdminByUsername(String username) {
        for (Admin admin : admins) {
            if (admin.getUsername().equalsIgnoreCase(username)) {
                return admin;
            }
        }
        return null;
    }

    /** A username is taken if ANY account (student or admin) already uses it. */
    public boolean isUsernameTaken(String username) {
        return findStudentByUsername(username) != null || findAdminByUsername(username) != null;
    }

    // ---------------------------------------------------------------- laboratories & computers

    /** Package-private: only DataInitializer may add laboratories. */
    void addLaboratory(Laboratory laboratory) {
        laboratories.add(laboratory);
    }

    public List<Laboratory> getLaboratories() {
        return Collections.unmodifiableList(laboratories);
    }

    public Laboratory getLaboratory(String name) {
        for (Laboratory lab : laboratories) {
            if (lab.getName().equals(name)) {
                return lab;
            }
        }
        return null;
    }

    public List<Computer> getAllComputers() {
        List<Computer> all = new ArrayList<>();
        for (Laboratory lab : laboratories) {
            all.addAll(lab.getComputers());
        }
        return all;
    }

    // ---------------------------------------------------------------- reservations

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    /** Creates IDs unique during this session. */
    public String nextReservationId() {
        reservationCounter++;
        return String.format("RES-%04d", reservationCounter);
    }

    public Reservation findConflict(Computer computer, LocalDate date, LocalTime start, LocalTime end) {
        for (Reservation reservation : reservations) {
            if (reservation.overlaps(computer, date, start, end)) {
                return reservation;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------- session

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /** Logging out clears ONLY the session. All other data stays. */
    public void logout() {
        this.currentUser = null;
    }

    /** Role check used by controllers. Throws SecurityException if the current user is not a student. */
    public Student requireStudent() {
        if (currentUser instanceof Student) {
            return (Student) currentUser;
        }
        throw new SecurityException("Access denied: please log in as a student.");
    }

    /** Role check used by controllers. Throws SecurityException if the current user is not an admin. */
    public Admin requireAdmin() {
        if (currentUser instanceof Admin) {
            return (Admin) currentUser;
        }
        throw new SecurityException("Access denied: administrator privileges are required.");
    }

    // ---------------------------------------------------------------- change notification

    /** A screen calls this once to say "run me whenever the data changes". */
    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    /** Controllers call this after they change something, so open screens can refresh. */
    public void fireDataChanged() {
        List<Runnable> copy = new ArrayList<>(changeListeners); // copy: listeners may unregister while running
        for (Runnable listener : copy) {
            listener.run();
        }
    }
}