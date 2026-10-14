package lars.model;


public abstract class User {

    private String fullName;
    private final String username;
    private String password;
    private final Role role;

    protected User(String fullName, String username, String password, Role role) {
        if (isBlank(fullName) || isBlank(username) || isBlank(password) || role == null) {
            throw new IllegalArgumentException("Full name, username, password and role are required.");
        }
        this.fullName = fullName.trim();
        this.username = username.trim();
        this.password = password;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    /** Compares the typed password with the stored one. The password itself is never exposed. */
    public boolean checkPassword(String typedPassword) {
        return typedPassword != null && password.equals(typedPassword);
    }

    /** Updates the name after checking that it is not empty. */
    public void setFullName(String newFullName) {
        if (isBlank(newFullName)) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = newFullName.trim();
    }

    public void changePassword(String newPassword) {
        if (isBlank(newPassword)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = newPassword;
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}