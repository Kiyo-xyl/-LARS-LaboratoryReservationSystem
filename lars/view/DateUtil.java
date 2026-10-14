package lars.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import lars.model.LabSchedule;

/** Converts between java.util.Date (used by JSpinner) and LocalDate (used everywhere else). */
public final class DateUtil {

	private DateUtil() {
		// utility class
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/** Today if it is a weekday, otherwise the coming Monday. Used as the default date. */
	public static LocalDate firstWeekdayFromToday() {
		LocalDate date = LocalDate.now();
		while (LabSchedule.isWeekend(date.getDayOfWeek())) {
			date = date.plusDays(1);
		}
		return date;
	}
}