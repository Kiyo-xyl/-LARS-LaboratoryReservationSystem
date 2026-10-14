package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lars.controller.ReservationController;
import lars.data.LARSData;
import lars.model.AvailabilityStatus;
import lars.model.Computer;
import lars.model.Reservation;
import lars.model.Student;

/**
 * The reservation form. The student's details are filled in automatically from the logged-in
 * account and cannot be edited (nobody can reserve under another identity).
 * ReservationController re-validates everything when Confirm is clicked.
 */
public class ReservationForm extends JPanel {

	private static final long serialVersionUID = 1L;

	private StudentDashboard dashboard;
	private Computer selectedComputer;
	private final ReservationController reservationController = new ReservationController();

	private JPanel pnlCard;
	private JPanel pnlButtons;
	private JLabel lblFormTitle;
	private JLabel lblFormSubtitle;
	private JLabel lblFullName;
	private JLabel lblStudentId;
	private JLabel lblCourse;
	private JLabel lblYearLevel;
	private JLabel lblSection;
	private JLabel lblLaboratory;
	private JLabel lblComputer;
	private JLabel lblAvailability;
	private JTextField txtFullName;
	private JTextField txtStudentId;
	private JTextField txtCourse;
	private JTextField txtYearLevel;
	private JTextField txtSection;
	private JTextField txtLaboratory;
	private JTextField txtComputer;
	private TimeSlotPicker timeSlotPicker;
	private JButton btnConfirm;
	private JButton btnBack;

	public ReservationForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 0));

		pnlCard = new JPanel();
		pnlCard.setBackground(Theme.WHITE);
		pnlCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(24, 32, 24, 32)));
		pnlCard.setLayout(new GridBagLayout());
		add(pnlCard, BorderLayout.NORTH);

		lblFormTitle = new JLabel("Reserve a Computer");
		lblFormTitle.setFont(Theme.FONT_HEADING);
		lblFormTitle.setForeground(Theme.NAVY);

		lblFormSubtitle = new JLabel("");
		lblFormSubtitle.setFont(Theme.FONT_BODY);
		lblFormSubtitle.setForeground(Theme.TEXT_MUTED);

		lblFullName = new JLabel("Full Name");
		lblFullName.setFont(Theme.FONT_LABEL);
		lblFullName.setForeground(Theme.TEXT_DARK);

		txtFullName = new JTextField();
		txtFullName.setColumns(18);
		txtFullName.setEditable(false);
		Theme.styleTextField(txtFullName);
		txtFullName.setBackground(Theme.LIGHT_GRAY);

		lblStudentId = new JLabel("Student ID");
		lblStudentId.setFont(Theme.FONT_LABEL);
		lblStudentId.setForeground(Theme.TEXT_DARK);

		txtStudentId = new JTextField();
		txtStudentId.setColumns(18);
		txtStudentId.setEditable(false);
		Theme.styleTextField(txtStudentId);
		txtStudentId.setBackground(Theme.LIGHT_GRAY);

		lblCourse = new JLabel("Program / Course");
		lblCourse.setFont(Theme.FONT_LABEL);
		lblCourse.setForeground(Theme.TEXT_DARK);

		txtCourse = new JTextField();
		txtCourse.setColumns(18);
		txtCourse.setEditable(false);
		Theme.styleTextField(txtCourse);
		txtCourse.setBackground(Theme.LIGHT_GRAY);

		lblYearLevel = new JLabel("Year Level");
		lblYearLevel.setFont(Theme.FONT_LABEL);
		lblYearLevel.setForeground(Theme.TEXT_DARK);

		txtYearLevel = new JTextField();
		txtYearLevel.setColumns(18);
		txtYearLevel.setEditable(false);
		Theme.styleTextField(txtYearLevel);
		txtYearLevel.setBackground(Theme.LIGHT_GRAY);

		lblSection = new JLabel("Section");
		lblSection.setFont(Theme.FONT_LABEL);
		lblSection.setForeground(Theme.TEXT_DARK);

		txtSection = new JTextField();
		txtSection.setColumns(18);
		txtSection.setEditable(false);
		Theme.styleTextField(txtSection);
		txtSection.setBackground(Theme.LIGHT_GRAY);

		lblLaboratory = new JLabel("Laboratory");
		lblLaboratory.setFont(Theme.FONT_LABEL);
		lblLaboratory.setForeground(Theme.TEXT_DARK);

		txtLaboratory = new JTextField();
		txtLaboratory.setColumns(18);
		txtLaboratory.setEditable(false);
		Theme.styleTextField(txtLaboratory);
		txtLaboratory.setBackground(Theme.LIGHT_GRAY);

		lblComputer = new JLabel("Computer Number");
		lblComputer.setFont(Theme.FONT_LABEL);
		lblComputer.setForeground(Theme.TEXT_DARK);

		txtComputer = new JTextField();
		txtComputer.setColumns(18);
		txtComputer.setEditable(false);
		Theme.styleTextField(txtComputer);
		txtComputer.setBackground(Theme.LIGHT_GRAY);

		timeSlotPicker = new TimeSlotPicker();

		lblAvailability = new JLabel(" ");
		lblAvailability.setFont(Theme.FONT_LABEL);
		lblAvailability.setForeground(Theme.TEXT_MUTED);

		pnlButtons = new JPanel();
		pnlButtons.setBackground(Theme.WHITE);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));

		btnConfirm = new JButton("Confirm Reservation");
		Theme.stylePrimaryButton(btnConfirm);

		btnBack = new JButton("Cancel");
		Theme.styleSecondaryButton(btnBack);

		pnlButtons.add(btnBack);
		pnlButtons.add(btnConfirm);

		GridBagConstraints gbc_lblFormTitle = new GridBagConstraints();
		gbc_lblFormTitle.gridwidth = 2;
		gbc_lblFormTitle.anchor = GridBagConstraints.WEST;
		gbc_lblFormTitle.insets = new Insets(0, 0, 2, 0);
		gbc_lblFormTitle.gridx = 0;
		gbc_lblFormTitle.gridy = 0;
		pnlCard.add(lblFormTitle, gbc_lblFormTitle);

		GridBagConstraints gbc_lblFormSubtitle = new GridBagConstraints();
		gbc_lblFormSubtitle.gridwidth = 2;
		gbc_lblFormSubtitle.anchor = GridBagConstraints.WEST;
		gbc_lblFormSubtitle.insets = new Insets(0, 0, 20, 0);
		gbc_lblFormSubtitle.gridx = 0;
		gbc_lblFormSubtitle.gridy = 1;
		pnlCard.add(lblFormSubtitle, gbc_lblFormSubtitle);

		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.anchor = GridBagConstraints.WEST;
		gbc_lblFullName.insets = new Insets(0, 0, 6, 24);
		gbc_lblFullName.gridx = 0;
		gbc_lblFullName.gridy = 2;
		pnlCard.add(lblFullName, gbc_lblFullName);

		GridBagConstraints gbc_txtFullName = new GridBagConstraints();
		gbc_txtFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFullName.anchor = GridBagConstraints.WEST;
		gbc_txtFullName.insets = new Insets(0, 0, 14, 24);
		gbc_txtFullName.weightx = 1.0;
		gbc_txtFullName.gridx = 0;
		gbc_txtFullName.gridy = 3;
		pnlCard.add(txtFullName, gbc_txtFullName);

		GridBagConstraints gbc_lblStudentId = new GridBagConstraints();
		gbc_lblStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblStudentId.insets = new Insets(0, 0, 6, 0);
		gbc_lblStudentId.gridx = 1;
		gbc_lblStudentId.gridy = 2;
		pnlCard.add(lblStudentId, gbc_lblStudentId);

		GridBagConstraints gbc_txtStudentId = new GridBagConstraints();
		gbc_txtStudentId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStudentId.anchor = GridBagConstraints.WEST;
		gbc_txtStudentId.insets = new Insets(0, 0, 14, 0);
		gbc_txtStudentId.weightx = 1.0;
		gbc_txtStudentId.gridx = 1;
		gbc_txtStudentId.gridy = 3;
		pnlCard.add(txtStudentId, gbc_txtStudentId);

		GridBagConstraints gbc_lblCourse = new GridBagConstraints();
		gbc_lblCourse.anchor = GridBagConstraints.WEST;
		gbc_lblCourse.insets = new Insets(0, 0, 6, 24);
		gbc_lblCourse.gridx = 0;
		gbc_lblCourse.gridy = 4;
		pnlCard.add(lblCourse, gbc_lblCourse);

		GridBagConstraints gbc_txtCourse = new GridBagConstraints();
		gbc_txtCourse.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCourse.anchor = GridBagConstraints.WEST;
		gbc_txtCourse.insets = new Insets(0, 0, 14, 24);
		gbc_txtCourse.weightx = 1.0;
		gbc_txtCourse.gridx = 0;
		gbc_txtCourse.gridy = 5;
		pnlCard.add(txtCourse, gbc_txtCourse);

		GridBagConstraints gbc_lblYearLevel = new GridBagConstraints();
		gbc_lblYearLevel.anchor = GridBagConstraints.WEST;
		gbc_lblYearLevel.insets = new Insets(0, 0, 6, 0);
		gbc_lblYearLevel.gridx = 1;
		gbc_lblYearLevel.gridy = 4;
		pnlCard.add(lblYearLevel, gbc_lblYearLevel);

		GridBagConstraints gbc_txtYearLevel = new GridBagConstraints();
		gbc_txtYearLevel.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYearLevel.anchor = GridBagConstraints.WEST;
		gbc_txtYearLevel.insets = new Insets(0, 0, 14, 0);
		gbc_txtYearLevel.weightx = 1.0;
		gbc_txtYearLevel.gridx = 1;
		gbc_txtYearLevel.gridy = 5;
		pnlCard.add(txtYearLevel, gbc_txtYearLevel);

		GridBagConstraints gbc_lblSection = new GridBagConstraints();
		gbc_lblSection.anchor = GridBagConstraints.WEST;
		gbc_lblSection.insets = new Insets(0, 0, 6, 24);
		gbc_lblSection.gridx = 0;
		gbc_lblSection.gridy = 6;
		pnlCard.add(lblSection, gbc_lblSection);

		GridBagConstraints gbc_txtSection = new GridBagConstraints();
		gbc_txtSection.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSection.anchor = GridBagConstraints.WEST;
		gbc_txtSection.insets = new Insets(0, 0, 14, 24);
		gbc_txtSection.weightx = 1.0;
		gbc_txtSection.gridx = 0;
		gbc_txtSection.gridy = 7;
		pnlCard.add(txtSection, gbc_txtSection);

		GridBagConstraints gbc_lblLaboratory = new GridBagConstraints();
		gbc_lblLaboratory.anchor = GridBagConstraints.WEST;
		gbc_lblLaboratory.insets = new Insets(0, 0, 6, 0);
		gbc_lblLaboratory.gridx = 1;
		gbc_lblLaboratory.gridy = 6;
		pnlCard.add(lblLaboratory, gbc_lblLaboratory);

		GridBagConstraints gbc_txtLaboratory = new GridBagConstraints();
		gbc_txtLaboratory.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLaboratory.anchor = GridBagConstraints.WEST;
		gbc_txtLaboratory.insets = new Insets(0, 0, 14, 0);
		gbc_txtLaboratory.weightx = 1.0;
		gbc_txtLaboratory.gridx = 1;
		gbc_txtLaboratory.gridy = 7;
		pnlCard.add(txtLaboratory, gbc_txtLaboratory);

		GridBagConstraints gbc_lblComputer = new GridBagConstraints();
		gbc_lblComputer.anchor = GridBagConstraints.WEST;
		gbc_lblComputer.insets = new Insets(0, 0, 6, 24);
		gbc_lblComputer.gridx = 0;
		gbc_lblComputer.gridy = 8;
		pnlCard.add(lblComputer, gbc_lblComputer);

		GridBagConstraints gbc_txtComputer = new GridBagConstraints();
		gbc_txtComputer.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComputer.anchor = GridBagConstraints.WEST;
		gbc_txtComputer.insets = new Insets(0, 0, 14, 24);
		gbc_txtComputer.weightx = 1.0;
		gbc_txtComputer.gridx = 0;
		gbc_txtComputer.gridy = 9;
		pnlCard.add(txtComputer, gbc_txtComputer);

		GridBagConstraints gbc_timeSlotPicker = new GridBagConstraints();
		gbc_timeSlotPicker.gridwidth = 2;
		gbc_timeSlotPicker.fill = GridBagConstraints.HORIZONTAL;
		gbc_timeSlotPicker.anchor = GridBagConstraints.WEST;
		gbc_timeSlotPicker.insets = new Insets(8, 0, 6, 0);
		gbc_timeSlotPicker.weightx = 1.0;
		gbc_timeSlotPicker.gridx = 0;
		gbc_timeSlotPicker.gridy = 10;
		pnlCard.add(timeSlotPicker, gbc_timeSlotPicker);

		GridBagConstraints gbc_lblAvailability = new GridBagConstraints();
		gbc_lblAvailability.gridwidth = 2;
		gbc_lblAvailability.anchor = GridBagConstraints.WEST;
		gbc_lblAvailability.insets = new Insets(0, 0, 16, 0);
		gbc_lblAvailability.gridx = 0;
		gbc_lblAvailability.gridy = 11;
		pnlCard.add(lblAvailability, gbc_lblAvailability);

		GridBagConstraints gbc_pnlButtons = new GridBagConstraints();
		gbc_pnlButtons.gridwidth = 2;
		gbc_pnlButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlButtons.anchor = GridBagConstraints.WEST;
		gbc_pnlButtons.weightx = 1.0;
		gbc_pnlButtons.gridx = 0;
		gbc_pnlButtons.gridy = 12;
		pnlCard.add(pnlButtons, gbc_pnlButtons);

		// ---- events ----
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleConfirm();
			}
		});

		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleBack();
			}
		});

		// When the date/time changes, re-check whether the chosen computer is still free.
		timeSlotPicker.addChangeListener(new Runnable() {
			@Override
			public void run() {
				updateAvailabilityMessage();
			}
		});
	}

	public void setDashboard(StudentDashboard dashboard) {
		this.dashboard = dashboard;
	}

	/** Called by the dashboard when a student clicks a green computer. */
	public void prepare(Computer computer, LocalDate date, LocalTime start, LocalTime end) {
		Student student = LARSData.getInstance().requireStudent(); // details always come from the logged-in student
		selectedComputer = computer;

		txtFullName.setText(student.getFullName());
		txtStudentId.setText(student.getStudentId());
		txtCourse.setText(student.getCourse());
		txtYearLevel.setText(student.getYearLevel());
		txtSection.setText(student.getSection());
		txtLaboratory.setText(computer.getLaboratoryName());
		txtComputer.setText(computer.getComputerNumber());

		timeSlotPicker.setLaboratory(computer.getLaboratoryName());
		timeSlotPicker.setSelection(date, start, end);
		btnConfirm.setEnabled(true);
		updateAvailabilityMessage();
	}

	// ---------------------------------------------------------------- logic

	/** Shows a live green/red message about the selected computer and time. */
	private void updateAvailabilityMessage() {
		if (selectedComputer == null) {
			return;
		}
		AvailabilityStatus status = reservationController.getAvailability(selectedComputer,
				timeSlotPicker.getDate(), timeSlotPicker.getStartTime(), timeSlotPicker.getEndTime());
		if (status == AvailabilityStatus.AVAILABLE) {
			lblAvailability.setText("This computer is available for the selected time.");
			lblAvailability.setForeground(Theme.AVAILABLE_GREEN);
		} else {
			lblAvailability.setText("Not available for the selected time (" + status.getLabel()
					+ "). Please change the date or time.");
			lblAvailability.setForeground(Theme.RESERVED_RED);
		}
	}

	/** Creates the reservation. The button is disabled first so a double click cannot submit twice. */
	private void handleConfirm() {
		btnConfirm.setEnabled(false);
		try {
			Reservation reservation = reservationController.createReservation(selectedComputer,
					timeSlotPicker.getDate(), timeSlotPicker.getStartTime(), timeSlotPicker.getEndTime());
			dashboard.showConfirmation(reservation);
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Reservation Failed", JOptionPane.WARNING_MESSAGE);
			btnConfirm.setEnabled(true);
		}
	}

	private void handleBack() {
		dashboard.showLaboratory(selectedComputer.getLaboratoryName());
	}
}