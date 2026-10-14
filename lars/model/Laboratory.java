package lars.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** A computer laboratory */
public class Laboratory {

    private final String name;
    private final List<Computer> computers = new ArrayList<>();
    private final Map<DayOfWeek, LabSchedule> schedules = new EnumMap<>(DayOfWeek.class);

    public Laboratory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addComputer(Computer computer) {
        computers.add(computer);
    }

    /** Read-only view so other classes cannot add or remove computers by accident. */
    public List<Computer> getComputers() {
        return Collections.unmodifiableList(computers);
    }

    public Computer findComputer(String computerNumber) {
        for (Computer computer : computers) {
            if (computer.getComputerNumber().equals(computerNumber)) {
                return computer;
            }
        }
        return null;
    }

    public void addSchedule(LabSchedule schedule) {
        schedules.put(schedule.getDay(), schedule);
    }

    public LabSchedule getSchedule(DayOfWeek day) {
        return schedules.get(day);
    }

    /** All seven schedules in Monday-to-Sunday order. */
    public List<LabSchedule> getSchedules() {
        return new ArrayList<>(schedules.values());
    }
}