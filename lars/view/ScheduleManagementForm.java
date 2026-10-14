package lars.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lars.controller.ScheduleController;
import lars.controller.Validator;
import lars.model.LabSchedule;
import lars.model.Reservation;

/**
 * Weekly schedule management, separately for COMLAB and ILAB.
 * Saturday and Sunday are always closed and their controls are disabled.
 * If a change would leave existing reservations outside the new hours, the administrator
 * must decide: cancel those reservations, or keep the current schedule.
 * (The seven rows are created in a loop, so this screen is edited in the Source tab rather than Design.)
 */
public class ScheduleManagementForm extends JPanel {

	private static final long serialVersionUID = 1L;

	/** Times offered in the dropdowns: 6:00 AM to 10:00 PM in 30-minute steps. */
	private static final List<LocalTime> TIME_OPTIONS = buildTimeOptions();

	private final ScheduleController scheduleController = new ScheduleController();
	private final List<DayRow> dayRows = new ArrayList<DayRow>(); // Monday ... Sunday

	private JPanel pnlTop;
	private JPanel pnlTitles;
	private JPanel pnlLabRow;
	private JPanel pnlCardHolder;
	private JPanel pnlSchedule;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private JLabel lblLaboratory;
	private JLabel lblNote;
	private JComboBox<String> cmbLaboratory;

	public ScheduleManagementForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 16));

		pnlTop = new JPanel();
		pnlTop.setOpaque(false);
		pnlTop.setLayout(new BorderLayout(0, 12));
		add(pnlTop, BorderLayout.NORTH);

		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 2));
		lblTitle = new JLabel("Schedule Management");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);
		lblSubtitle = new JLabel("");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);
		pnlTop.add(pnlTitles, BorderLayout.NORTH);

		pnlLabRow = new JPanel();
		pnlLabRow.setOpaque(false);
		pnlLabRow.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		lblLaboratory = new JLabel("Laboratory");
		lblLaboratory.setFont(Theme.FONT_LABEL);
		lblLaboratory.setForeground(Theme.TEXT_DARK);
		pnlLabRow.add(lblLaboratory);
		cmbLaboratory = new JComboBox<String>(new String[] {"COMLAB", "ILAB"});
		Theme.styleComboBox(cmbLaboratory);
		pnlLabRow.add(cmbLaboratory);
		pnlTop.add(pnlLabRow, BorderLayout.CENTER);

		// ---- the schedule grid inside a white card ----
		pnlCardHolder = new JPanel();
		pnlCardHolder.setOpaque(false);
		pnlCardHolder.setLayout(new BorderLayout(0, 0));
		add(pnlCardHolder, BorderLayout.CENTER);

		pnlSchedule = new JPanel();
		pnlSchedule.setBackground(Theme.WHITE);
		pnlSchedule.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(18, 24, 18, 24)));
		pnlSchedule.setLayout(new GridBagLayout());
		pnlCardHolder.add(pnlSchedule, BorderLayout.NORTH);

		addCell(createHeaderLabel("Day"), 0, 0);
		addCell(createHeaderLabel("Status"), 1, 0);
		addCell(createHeaderLabel("Opening Time"), 2, 0);
		addCell(createHeaderLabel("Closing Time"), 3, 0);
		addCell(createHeaderLabel(" "), 4, 0);

		int y = 1;
		for (DayOfWeek day : DayOfWeek.values()) { // Monday ... Sunday
			DayRow row = new DayRow(day);
			dayRows.add(row);
			addCell(row.lblDay, 0, y);
			addCell(row.chkOpen, 1, y);
			addCell(row.cmbOpening, 2, y);
			addCell(row.cmbClosing, 3, y);
			addCell(row.btnSave, 4, y);
			y++;
		}

		// an empty, stretchy last column pushes everything to the left
		GridBagConstraints gbcFiller = new GridBagConstraints();
		gbcFiller.weightx = 1.0;
		gbcFiller.gridx = 5;
		gbcFiller.gridy = 0;
		pnlSchedule.add(new JLabel(" "), gbcFiller);

		lblNote = new JLabel("Saturday and Sunday are always closed and cannot be opened.");
		lblNote.setFont(Theme.FONT_SMALL);
		lblNote.setForeground(Theme.TEXT_MUTED);
		add(lblNote, BorderLayout.SOUTH);

		cmbLaboratory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
	}

	/** Loads the saved schedule of the chosen laboratory into the rows (discarding unsaved edits). */
	public void refresh() {
		String laboratory = (String) cmbLaboratory.getSelectedItem();
		for (LabSchedule schedule : scheduleController.getSchedules(laboratory)) {
			dayRows.get(schedule.getDay().ordinal()).load(schedule);
		}
	}

	// ---------------------------------------------------------------- saving one day

	private void saveRow(DayRow row) {
		String laboratory = (String) cmbLaboratory.getSelectedItem();
		String dayName = row.day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		boolean open = row.chkOpen.isSelected();
		LocalTime opening = TIME_OPTIONS.get(row.cmbOpening.getSelectedIndex());
		LocalTime closing = TIME_OPTIONS.get(row.cmbClosing.getSelectedIndex());

		try {
			// 1. Which upcoming reservations would no longer fit?
			List<Reservation> conflicts = scheduleController.findConflicts(laboratory, row.day, open, opening, closing);

			// 2. If there are any, the administrator must decide.
			boolean cancelConflicts = false;
			if (!conflicts.isEmpty()) {
				if (!askAboutConflicts(laboratory, dayName, conflicts)) {
					refresh(); // keep the current schedule
					return;
				}
				cancelConflicts = true;
			}

			// 3. Apply the change (the controller cancels the conflicts only if told to).
			int cancelled = scheduleController.updateSchedule(laboratory, row.day, open, opening, closing, cancelConflicts);
			String message = laboratory + " schedule for " + dayName + " has been updated.";
			if (cancelled > 0) {
				message += "\n" + cancelled + " conflicting reservation(s) were cancelled.";
			}
			JOptionPane.showMessageDialog(this, message, "Schedule Updated", JOptionPane.INFORMATION_MESSAGE);
			refresh();
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Schedule Not Updated", JOptionPane.WARNING_MESSAGE);
			refresh();
		}
	}

	/** Shows the conflicts. Returns true if the administrator chose to cancel them and apply the change. */
	private boolean askAboutConflicts(String laboratory, String dayName, List<Reservation> conflicts) {
		StringBuilder text = new StringBuilder();
		text.append(conflicts.size()).append(" upcoming reservation(s) on ").append(dayName)
				.append("s in ").append(laboratory).append(" do not fit the new schedule:\n\n");
		int shown = 0;
		for (Reservation r : conflicts) {
			if (shown == 5) {
				text.append("... and ").append(conflicts.size() - 5).append(" more\n");
				break;
			}
			text.append(r.getReservationId()).append(" - ").append(r.getStudent().getFullName())
					.append(" - ").append(r.getComputer().getComputerNumber())
					.append(", ").append(Validator.DATE_FORMAT.format(r.getDate()))
					.append(" ").append(Validator.TIME_FORMAT.format(r.getStartTime()))
					.append(" to ").append(Validator.TIME_FORMAT.format(r.getEndTime())).append("\n");
			shown++;
		}
		text.append("\nWhat would you like to do?");

		Object[] options = {"Cancel Those Reservations and Apply", "Keep Current Schedule"};
		int choice = JOptionPane.showOptionDialog(this, text.toString(), "Schedule Conflict",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		return choice == 0;
	}

	// ---------------------------------------------------------------- small helpers

	private JLabel createHeaderLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Theme.FONT_LABEL);
		label.setForeground(Theme.TEXT_MUTED);
		return label;
	}

	private void addCell(Component component, int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 0, 4, 24);
		gbc.gridx = x;
		gbc.gridy = y;
		pnlSchedule.add(component, gbc);
	}

	private static List<LocalTime> buildTimeOptions() {
		List<LocalTime> options = new ArrayList<LocalTime>();
		for (int minutes = 6 * 60; minutes <= 22 * 60; minutes += 30) {
			options.add(LocalTime.of(minutes / 60, minutes % 60));
		}
		return options;
	}

	private static String[] timeLabels() {
		String[] labels = new String[TIME_OPTIONS.size()];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = Validator.TIME_FORMAT.format(TIME_OPTIONS.get(i));
		}
		return labels;
	}

	// ---------------------------------------------------------------- one row of the grid

	/** The controls for one weekday. */
	private class DayRow {

		private final DayOfWeek day;
		private final JLabel lblDay;
		private final JCheckBox chkOpen;
		private final JComboBox<String> cmbOpening;
		private final JComboBox<String> cmbClosing;
		private final JButton btnSave;

		DayRow(DayOfWeek dayOfWeek) {
			this.day = dayOfWeek;

			lblDay = new JLabel(day.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
			lblDay.setFont(Theme.FONT_LABEL);
			lblDay.setForeground(Theme.TEXT_DARK);

			chkOpen = new JCheckBox(LabSchedule.isWeekend(day) ? "Closed" : "Open");
			chkOpen.setFont(Theme.FONT_BODY);
			chkOpen.setBackground(Theme.WHITE);

			cmbOpening = new JComboBox<String>(timeLabels());
			Theme.styleComboBox(cmbOpening);
			cmbClosing = new JComboBox<String>(timeLabels());
			Theme.styleComboBox(cmbClosing);

			btnSave = new JButton("Save");
			Theme.stylePrimaryButton(btnSave);

			// The time boxes only matter while the day is open.
			chkOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cmbOpening.setEnabled(chkOpen.isSelected());
					cmbClosing.setEnabled(chkOpen.isSelected());
				}
			});
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveRow(DayRow.this);
				}
			});
		}

		/** Shows a saved schedule. Weekend rows are shown as Closed with every control disabled. */
		void load(LabSchedule schedule) {
			boolean weekend = LabSchedule.isWeekend(day);
			chkOpen.setSelected(schedule.isOpen());

			int openingIndex = TIME_OPTIONS.indexOf(schedule.getOpeningTime());
			cmbOpening.setSelectedIndex(openingIndex >= 0 ? openingIndex : 0);
			int closingIndex = TIME_OPTIONS.indexOf(schedule.getClosingTime());
			cmbClosing.setSelectedIndex(closingIndex >= 0 ? closingIndex : TIME_OPTIONS.size() - 1);

			chkOpen.setEnabled(!weekend);
			cmbOpening.setEnabled(!weekend && schedule.isOpen());
			cmbClosing.setEnabled(!weekend && schedule.isOpen());
			btnSave.setEnabled(!weekend);
			btnSave.setBackground(weekend ? Theme.BORDER_GRAY : Theme.PRIMARY);
			btnSave.setForeground(weekend ? Theme.TEXT_MUTED : Theme.WHITE);
		}
	}
}