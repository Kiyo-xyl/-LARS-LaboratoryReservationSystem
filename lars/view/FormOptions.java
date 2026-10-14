package lars.view;

import javax.swing.JComboBox;

/** The choices shown in the Course and Year Level dropdowns (used by several screens). */
public final class FormOptions {

	// Index 0 is a "please choose" prompt, not a real value.
	private static final String[] COURSES = {
		"Select course",
		"BS Information Systems (BSIS)",
		"BS Hospitality Management (BSHM)",
		"BS Tourism Management (BSTM)",
		"BS Education (BSED)",
		"Bachelor of Public Ads (BPA)"
	};

	private static final String[] YEAR_LEVELS = {
		"Select year level", "1st Year", "2nd Year", "3rd Year", "4th Year"
	};

	private FormOptions() {
		// utility class
	}

	/** A copy, so no screen can accidentally change the original list. */
	public static String[] getCourses() {
		return COURSES.clone();
	}

	public static String[] getYearLevels() {
		return YEAR_LEVELS.clone();
	}

	/** Returns the chosen text, or "" if the user left the "Select..." prompt. */
	public static String valueOrEmpty(JComboBox<String> combo) {
		if (combo.getSelectedIndex() <= 0) {
			return "";
		}
		return (String) combo.getSelectedItem();
	}

	/** Selects the combo item equal to value; falls back to the "Select..." prompt if it is not found. */
	public static void selectValue(JComboBox<String> combo, String value) {
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).equals(value)) {
				combo.setSelectedIndex(i);
				return;
			}
		}
		combo.setSelectedIndex(0);
	}
}