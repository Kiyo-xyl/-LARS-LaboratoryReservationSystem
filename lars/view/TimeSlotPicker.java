package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lars.controller.ReservationController;
import lars.controller.Validator;
import lars.model.LabSchedule;

/**
 * A small reusable control: Date + Start Time + End Time for ONE laboratory.
 * Used by the laboratory screens and by the reservation form, so the time logic is written once.
 * The time dropdowns always follow the laboratory's current schedule for the chosen date.
 */
public class TimeSlotPicker extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ReservationController reservationController = new ReservationController();
	private final List<Runnable> changeListeners = new ArrayList<Runnable>();
	private List<LocalTime> slots = new ArrayList<LocalTime>(); // opening time ... closing time
	private String laboratoryName;
	private boolean updating = false; // true while WE change the controls, so their events are ignored

	private JPanel pnlRow;
	private JLabel lblDate;
	private JLabel lblStartTime;
	private JLabel lblEndTime;
	private JLabel lblHours;
	private JSpinner spnDate;
	private JComboBox<String> cmbStartTime;
	private JComboBox<String> cmbEndTime;

	public TimeSlotPicker() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 8));

		pnlRow = new JPanel();
		pnlRow.setOpaque(false);
		pnlRow.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
		add(pnlRow, BorderLayout.NORTH);

		lblDate = new JLabel("Date");
		lblDate.setFont(Theme.FONT_LABEL);
		lblDate.setForeground(Theme.TEXT_DARK);
		pnlRow.add(lblDate);

		spnDate = new JSpinner(new SpinnerDateModel());
		spnDate.setEditor(new JSpinner.DateEditor(spnDate, "EEE, MMM dd, yyyy"));
		spnDate.setFont(Theme.FONT_BODY);
		spnDate.setValue(DateUtil.toDate(DateUtil.firstWeekdayFromToday()));
		pnlRow.add(spnDate);

		lblStartTime = new JLabel("Start Time");
		lblStartTime.setFont(Theme.FONT_LABEL);
		lblStartTime.setForeground(Theme.TEXT_DARK);
		pnlRow.add(lblStartTime);

		cmbStartTime = new JComboBox<String>();
		Theme.styleComboBox(cmbStartTime);
		pnlRow.add(cmbStartTime);

		lblEndTime = new JLabel("End Time");
		lblEndTime.setFont(Theme.FONT_LABEL);
		lblEndTime.setForeground(Theme.TEXT_DARK);
		pnlRow.add(lblEndTime);

		cmbEndTime = new JComboBox<String>();
		Theme.styleComboBox(cmbEndTime);
		pnlRow.add(cmbEndTime);

		lblHours = new JLabel(" ");
		lblHours.setFont(Theme.FONT_SMALL);
		lblHours.setForeground(Theme.TEXT_MUTED);
		add(lblHours, BorderLayout.SOUTH);

		// ---- events ----
		spnDate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating) {
					reloadSlots(getStartTime(), getEndTime()); // new date = new schedule, keep times if possible
				}
			}
		});
		ActionListener comboListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating) {
					updateInfoLabel();
					fireChanged();
				}
			}
		};
		cmbStartTime.addActionListener(comboListener);
		cmbEndTime.addActionListener(comboListener);
	}

	// ---------------------------------------------------------------- public methods

	/** Chooses which laboratory's schedule the time dropdowns follow. */
	public void setLaboratory(String laboratoryName) {
		this.laboratoryName = laboratoryName;
		reloadSlots(getStartTime(), getEndTime());
	}

	/** Sets all three controls (used when the reservation form opens). */
	public void setSelection(LocalDate date, LocalTime start, LocalTime end) {
		updating = true;
		spnDate.setValue(DateUtil.toDate(date));
		updating = false;
		reloadSlots(start, end);
	}

	public LocalDate getDate() {
		return DateUtil.toLocalDate((Date) spnDate.getValue());
	}

	/** Selected start time, or null when the laboratory is closed on the chosen date. */
	public LocalTime getStartTime() {
		int index = cmbStartTime.getSelectedIndex();
		if (slots.size() < 2 || index < 0) {
			return null;
		}
		return slots.get(index);
	}

	/** Selected end time, or null when the laboratory is closed on the chosen date. */
	public LocalTime getEndTime() {
		int index = cmbEndTime.getSelectedIndex();
		if (slots.size() < 2 || index < 0) {
			return null;
		}
		return slots.get(index + 1); // the end list starts at the second slot
	}

	/** Screens register here to be told when the date or times change. */
	public void addChangeListener(Runnable listener) {
		changeListeners.add(listener);
	}

	// ---------------------------------------------------------------- internal logic

	/** Refills both dropdowns from the schedule of the chosen date. */
	private void reloadSlots(LocalTime wantedStart, LocalTime wantedEnd) {
		updating = true;
		if (laboratoryName == null) {
			slots = new ArrayList<LocalTime>();
		} else {
			slots = reservationController.getTimeSlots(laboratoryName, getDate());
		}
		cmbStartTime.removeAllItems();
		cmbEndTime.removeAllItems();

		boolean open = slots.size() >= 2;
		if (open) {
			// Start can be any slot except the last; end can be any slot except the first.
			for (int i = 0; i < slots.size(); i++) {
				String text = Validator.TIME_FORMAT.format(slots.get(i));
				if (i < slots.size() - 1) {
					cmbStartTime.addItem(text);
				}
				if (i > 0) {
					cmbEndTime.addItem(text);
				}
			}
			int startIndex = slots.indexOf(wantedStart);
			if (startIndex < 0 || startIndex >= slots.size() - 1) {
				startIndex = defaultStartIndex();
			}
			cmbStartTime.setSelectedIndex(startIndex);

			int endSlot = slots.indexOf(wantedEnd);
			if (endSlot <= startIndex) {
				int oneHour = 60 / ReservationController.TIME_STEP_MINUTES;
				endSlot = Math.min(startIndex + oneHour, slots.size() - 1); // default: 1 hour
			}
			cmbEndTime.setSelectedIndex(endSlot - 1);
		}
		cmbStartTime.setEnabled(open);
		cmbEndTime.setEnabled(open);
		updating = false;

		updateInfoLabel();
		fireChanged();
	}

	/** For today, start at the first time that has not passed yet; otherwise at opening time. */
	private int defaultStartIndex() {
		if (getDate().equals(LocalDate.now())) {
			for (int i = 0; i < slots.size() - 1; i++) {
				if (!slots.get(i).isBefore(LocalTime.now())) {
					return i;
				}
			}
		}
		return 0;
	}

	/** The small line under the controls: opening hours, or a clear problem message. */
	private void updateInfoLabel() {
		if (laboratoryName == null) {
			lblHours.setText(" ");
			return;
		}
		LocalDate date = getDate();
		if (slots.size() < 2) {
			if (LabSchedule.isWeekend(date.getDayOfWeek())) {
				lblHours.setText(laboratoryName + " is closed on Saturdays and Sundays.");
			} else {
				lblHours.setText(laboratoryName + " is closed on this day.");
			}
			lblHours.setForeground(Theme.RESERVED_RED);
		} else if (date.isBefore(LocalDate.now())) {
			lblHours.setText("This date has already passed. Please choose today or a later date.");
			lblHours.setForeground(Theme.RESERVED_RED);
		} else if (!getEndTime().isAfter(getStartTime())) {
			lblHours.setText("The end time must be later than the start time.");
			lblHours.setForeground(Theme.RESERVED_RED);
		} else {
			lblHours.setText(laboratoryName + " is open "
					+ Validator.TIME_FORMAT.format(slots.get(0)) + " to "
					+ Validator.TIME_FORMAT.format(slots.get(slots.size() - 1)) + " on this day.");
			lblHours.setForeground(Theme.TEXT_MUTED);
		}
	}

	private void fireChanged() {
		for (Runnable listener : changeListeners) {
			listener.run();
		}
	}
}