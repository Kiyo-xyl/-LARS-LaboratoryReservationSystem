package lars.data;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lars.model.Admin;
import lars.model.Computer;
import lars.model.LabSchedule;
import lars.model.Laboratory;

/**
 * Creates the default data every time LARS starts:
 * DEMO ADMIN LOGIN :
 *     username: admin
 *     password: admin123
 */
public final class DataInitializer {

    public static final String DEMO_ADMIN_USERNAME = "admin";
    public static final String DEMO_ADMIN_PASSWORD = "admin123";
    public static final int COMPUTERS_PER_LAB = 20;

    private DataInitializer() {
        // utility class 
    }

    /** Package-private: called once by the LARSData constructor. */
    static void loadDefaults(LARSData data) {
        data.addAdmin(new Admin("System Administrator", DEMO_ADMIN_USERNAME, DEMO_ADMIN_PASSWORD));
        data.addLaboratory(createLaboratory("COMLAB"));
        data.addLaboratory(createLaboratory("ILAB"));
    }

    private static Laboratory createLaboratory(String name) {
        Laboratory lab = new Laboratory(name);

        // PC01 ... PC20 
        for (int i = 1; i <= COMPUTERS_PER_LAB; i++) {
            lab.addComputer(new Computer(name, String.format("PC%02d", i)));
        }

        // Monday-Friday open 8:00 AM - 5:00 PM, Saturday and Sunday closed
        LocalTime opening = LocalTime.of(8, 0);
        LocalTime closing = LocalTime.of(17, 0);
        for (DayOfWeek day : DayOfWeek.values()) {
            boolean open = !LabSchedule.isWeekend(day);
            lab.addSchedule(new LabSchedule(day, opening, closing, open));
        }
        return lab;
    }
}