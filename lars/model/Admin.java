package lars.model;

/** The administrator account. A default one is created */
public class Admin extends User {

    public Admin(String fullName, String username, String password) {
        super(fullName, username, password, Role.ADMIN);
    }
}