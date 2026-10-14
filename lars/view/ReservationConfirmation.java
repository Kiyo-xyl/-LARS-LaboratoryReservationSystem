package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lars.controller.Validator;
import lars.model.Reservation;
import lars.model.Student;

/** Shown only AFTER a reservation passed every check and was saved in memory. */
public class ReservationConfirmation extends JPanel {

	private static final long serialVersionUID = 1L;

	private StudentDashboard dashboard;

	private JPanel pnlCard;
	private JPanel pnlButtons;
	private JLabel lblSuccess;
	private JLabel lblSuccessNote;
	private JLabel lblCapReservationId;
	private JLabel lblValueReservationId;
	private JLabel lblCapStudentName;
	private JLabel lblValueStudentName;
	private JLabel lblCapStudentId;
	private JLabel lblValueStudentId;
	private JLabel lblCapCourse;
	private JLabel lblValueCourse;
	private JLabel lblCapYearSection;
	private JLabel lblValueYearSection;
	private JLabel lblCapLaboratory;
	private JLabel lblValueLaboratory;
	private JLabel lblCapComputer;
	private JLabel lblValueComputer;
	private JLabel lblCapDate;
	private JLabel lblValueDate;
	private JLabel lblCapStartTime;
	private JLabel lblValueStartTime;
	private JLabel lblCapEndTime;
	private JLabel lblValueEndTime;
	private JLabel lblCapStatus;
	private JLabel lblValueStatus;
	private JButton btnBackToDashboard;
	private JButton btnViewReservations;

	public ReservationConfirmation() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 0));

		pnlCard = new JPanel();
		pnlCard.setBackground(Theme.WHITE);
		pnlCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(28, 40, 28, 40)));
		pnlCard.setLayout(new GridBagLayout());
		add(pnlCard, BorderLayout.NORTH);

		lblSuccess = new JLabel("Reservation successful! Your computer has been reserved.");
		lblSuccess.setFont(Theme.FONT_HEADING);
		lblSuccess.setForeground(Theme.AVAILABLE_GREEN);

		lblSuccessNote = new JLabel("Please keep your Reservation ID. You can review or cancel it in My Reservations.");
		lblSuccessNote.setFont(Theme.FONT_BODY);
		lblSuccessNote.setForeground(Theme.TEXT_MUTED);

		lblCapReservationId = new JLabel("Reservation ID");
		lblCapReservationId.setFont(Theme.FONT_BODY);
		lblCapReservationId.setForeground(Theme.TEXT_MUTED);

		lblValueReservationId = new JLabel(" ");
		lblValueReservationId.setFont(Theme.FONT_LABEL);
		lblValueReservationId.setForeground(Theme.TEXT_DARK);

		lblCapStudentName = new JLabel("Student Name");
		lblCapStudentName.setFont(Theme.FONT_BODY);
		lblCapStudentName.setForeground(Theme.TEXT_MUTED);

		lblValueStudentName = new JLabel(" ");
		lblValueStudentName.setFont(Theme.FONT_LABEL);
		lblValueStudentName.setForeground(Theme.TEXT_DARK);

		lblCapStudentId = new JLabel("Student ID");
		lblCapStudentId.setFont(Theme.FONT_BODY);
		lblCapStudentId.setForeground(Theme.TEXT_MUTED);

		lblValueStudentId = new JLabel(" ");
		lblValueStudentId.setFont(Theme.FONT_LABEL);
		lblValueStudentId.setForeground(Theme.TEXT_DARK);

		lblCapCourse = new JLabel("Program / Course");
		lblCapCourse.setFont(Theme.FONT_BODY);
		lblCapCourse.setForeground(Theme.TEXT_MUTED);

		lblValueCourse = new JLabel(" ");
		lblValueCourse.setFont(Theme.FONT_LABEL);
		lblValueCourse.setForeground(Theme.TEXT_DARK);

		lblCapYearSection = new JLabel("Year Level and Section");
		lblCapYearSection.setFont(Theme.FONT_BODY);
		lblCapYearSection.setForeground(Theme.TEXT_MUTED);

		lblValueYearSection = new JLabel(" ");
		lblValueYearSection.setFont(Theme.FONT_LABEL);
		lblValueYearSection.setForeground(Theme.TEXT_DARK);

		lblCapLaboratory = new JLabel("Laboratory");
		lblCapLaboratory.setFont(Theme.FONT_BODY);
		lblCapLaboratory.setForeground(Theme.TEXT_MUTED);

		lblValueLaboratory = new JLabel(" ");
		lblValueLaboratory.setFont(Theme.FONT_LABEL);
		lblValueLaboratory.setForeground(Theme.TEXT_DARK);

		lblCapComputer = new JLabel("Computer Number");
		lblCapComputer.setFont(Theme.FONT_BODY);
		lblCapComputer.setForeground(Theme.TEXT_MUTED);

		lblValueComputer = new JLabel(" ");
		lblValueComputer.setFont(Theme.FONT_LABEL);
		lblValueComputer.setForeground(Theme.TEXT_DARK);

		lblCapDate = new JLabel("Reservation Date");
		lblCapDate.setFont(Theme.FONT_BODY);
		lblCapDate.setForeground(Theme.TEXT_MUTED);

		lblValueDate = new JLabel(" ");
		lblValueDate.setFont(Theme.FONT_LABEL);
		lblValueDate.setForeground(Theme.TEXT_DARK);

		lblCapStartTime = new JLabel("Start Time");
		lblCapStartTime.setFont(Theme.FONT_BODY);
		lblCapStartTime.setForeground(Theme.TEXT_MUTED);

		lblValueStartTime = new JLabel(" ");
		lblValueStartTime.setFont(Theme.FONT_LABEL);
		lblValueStartTime.setForeground(Theme.TEXT_DARK);

		lblCapEndTime = new JLabel("End Time");
		lblCapEndTime.setFont(Theme.FONT_BODY);
		lblCapEndTime.setForeground(Theme.TEXT_MUTED);

		lblValueEndTime = new JLabel(" ");
		lblValueEndTime.setFont(Theme.FONT_LABEL);
		lblValueEndTime.setForeground(Theme.TEXT_DARK);

		lblCapStatus = new JLabel("Reservation Status");
		lblCapStatus.setFont(Theme.FONT_BODY);
		lblCapStatus.setForeground(Theme.TEXT_MUTED);

		lblValueStatus = new JLabel(" ");
		lblValueStatus.setFont(Theme.FONT_LABEL);
		lblValueStatus.setForeground(Theme.TEXT_DARK);

		pnlButtons = new JPanel();
		pnlButtons.setBackground(Theme.WHITE);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));

		btnBackToDashboard = new JButton("Back to Dashboard");
		Theme.stylePrimaryButton(btnBackToDashboard);

		btnViewReservations = new JButton("View My Reservations");
		Theme.styleSecondaryButton(btnViewReservations);

		pnlButtons.add(btnBackToDashboard);
		pnlButtons.add(btnViewReservations);

		GridBagConstraints gbc_lblSuccess = new GridBagConstraints();
		gbc_lblSuccess.gridwidth = 2;
		gbc_lblSuccess.anchor = GridBagConstraints.WEST;
		gbc_lblSuccess.insets = new Insets(0, 0, 4, 0);
		gbc_lblSuccess.gridx = 0;
		gbc_lblSuccess.gridy = 0;
		pnlCard.add(lblSuccess, gbc_lblSuccess);

		GridBagConstraints gbc_lblSuccessNote = new GridBagConstraints();
		gbc_lblSuccessNote.gridwidth = 2;
		gbc_lblSuccessNote.anchor = GridBagConstraints.WEST;
		gbc_lblSuccessNote.insets = new Insets(0, 0, 22, 0);
		gbc_lblSuccessNote.gridx = 0;
		gbc_lblSuccessNote.gridy = 1;
		pnlCard.add(lblSuccessNote, gbc_lblSuccessNote);

		GridBagConstraints gbc_lblCapReservationId = new GridBagConstraints();
		gbc_lblCapReservationId.anchor = GridBagConstraints.WEST;
		gbc_lblCapReservationId.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapReservationId.gridx = 0;
		gbc_lblCapReservationId.gridy = 2;
		pnlCard.add(lblCapReservationId, gbc_lblCapReservationId);

		GridBagConstraints gbc_lblValueReservationId = new GridBagConstraints();
		gbc_lblValueReservationId.anchor = GridBagConstraints.WEST;
		gbc_lblValueReservationId.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueReservationId.weightx = 1.0;
		gbc_lblValueReservationId.gridx = 1;
		gbc_lblValueReservationId.gridy = 2;
		pnlCard.add(lblValueReservationId, gbc_lblValueReservationId);

		GridBagConstraints gbc_lblCapStudentName = new GridBagConstraints();
		gbc_lblCapStudentName.anchor = GridBagConstraints.WEST;
		gbc_lblCapStudentName.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapStudentName.gridx = 0;
		gbc_lblCapStudentName.gridy = 3;
		pnlCard.add(lblCapStudentName, gbc_lblCapStudentName);

		GridBagConstraints gbc_lblValueStudentName = new GridBagConstraints();
		gbc_lblValueStudentName.anchor = GridBagConstraints.WEST;
		gbc_lblValueStudentName.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueStudentName.weightx = 1.0;
		gbc_lblValueStudentName.gridx = 1;
		gbc_lblValueStudentName.gridy = 3;
		pnlCard.add(lblValueStudentName, gbc_lblValueStudentName);

		GridBagConstraints gbc_lblCapStudentId = new GridBagConstraints();
		gbc_lblCapStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblCapStudentId.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapStudentId.gridx = 0;
		gbc_lblCapStudentId.gridy = 4;
		pnlCard.add(lblCapStudentId, gbc_lblCapStudentId);

		GridBagConstraints gbc_lblValueStudentId = new GridBagConstraints();
		gbc_lblValueStudentId.anchor = GridBagConstraints.WEST;
		gbc_lblValueStudentId.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueStudentId.weightx = 1.0;
		gbc_lblValueStudentId.gridx = 1;
		gbc_lblValueStudentId.gridy = 4;
		pnlCard.add(lblValueStudentId, gbc_lblValueStudentId);

		GridBagConstraints gbc_lblCapCourse = new GridBagConstraints();
		gbc_lblCapCourse.anchor = GridBagConstraints.WEST;
		gbc_lblCapCourse.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapCourse.gridx = 0;
		gbc_lblCapCourse.gridy = 5;
		pnlCard.add(lblCapCourse, gbc_lblCapCourse);

		GridBagConstraints gbc_lblValueCourse = new GridBagConstraints();
		gbc_lblValueCourse.anchor = GridBagConstraints.WEST;
		gbc_lblValueCourse.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueCourse.weightx = 1.0;
		gbc_lblValueCourse.gridx = 1;
		gbc_lblValueCourse.gridy = 5;
		pnlCard.add(lblValueCourse, gbc_lblValueCourse);

		GridBagConstraints gbc_lblCapYearSection = new GridBagConstraints();
		gbc_lblCapYearSection.anchor = GridBagConstraints.WEST;
		gbc_lblCapYearSection.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapYearSection.gridx = 0;
		gbc_lblCapYearSection.gridy = 6;
		pnlCard.add(lblCapYearSection, gbc_lblCapYearSection);

		GridBagConstraints gbc_lblValueYearSection = new GridBagConstraints();
		gbc_lblValueYearSection.anchor = GridBagConstraints.WEST;
		gbc_lblValueYearSection.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueYearSection.weightx = 1.0;
		gbc_lblValueYearSection.gridx = 1;
		gbc_lblValueYearSection.gridy = 6;
		pnlCard.add(lblValueYearSection, gbc_lblValueYearSection);

		GridBagConstraints gbc_lblCapLaboratory = new GridBagConstraints();
		gbc_lblCapLaboratory.anchor = GridBagConstraints.WEST;
		gbc_lblCapLaboratory.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapLaboratory.gridx = 0;
		gbc_lblCapLaboratory.gridy = 7;
		pnlCard.add(lblCapLaboratory, gbc_lblCapLaboratory);

		GridBagConstraints gbc_lblValueLaboratory = new GridBagConstraints();
		gbc_lblValueLaboratory.anchor = GridBagConstraints.WEST;
		gbc_lblValueLaboratory.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueLaboratory.weightx = 1.0;
		gbc_lblValueLaboratory.gridx = 1;
		gbc_lblValueLaboratory.gridy = 7;
		pnlCard.add(lblValueLaboratory, gbc_lblValueLaboratory);

		GridBagConstraints gbc_lblCapComputer = new GridBagConstraints();
		gbc_lblCapComputer.anchor = GridBagConstraints.WEST;
		gbc_lblCapComputer.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapComputer.gridx = 0;
		gbc_lblCapComputer.gridy = 8;
		pnlCard.add(lblCapComputer, gbc_lblCapComputer);

		GridBagConstraints gbc_lblValueComputer = new GridBagConstraints();
		gbc_lblValueComputer.anchor = GridBagConstraints.WEST;
		gbc_lblValueComputer.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueComputer.weightx = 1.0;
		gbc_lblValueComputer.gridx = 1;
		gbc_lblValueComputer.gridy = 8;
		pnlCard.add(lblValueComputer, gbc_lblValueComputer);

		GridBagConstraints gbc_lblCapDate = new GridBagConstraints();
		gbc_lblCapDate.anchor = GridBagConstraints.WEST;
		gbc_lblCapDate.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapDate.gridx = 0;
		gbc_lblCapDate.gridy = 9;
		pnlCard.add(lblCapDate, gbc_lblCapDate);

		GridBagConstraints gbc_lblValueDate = new GridBagConstraints();
		gbc_lblValueDate.anchor = GridBagConstraints.WEST;
		gbc_lblValueDate.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueDate.weightx = 1.0;
		gbc_lblValueDate.gridx = 1;
		gbc_lblValueDate.gridy = 9;
		pnlCard.add(lblValueDate, gbc_lblValueDate);

		GridBagConstraints gbc_lblCapStartTime = new GridBagConstraints();
		gbc_lblCapStartTime.anchor = GridBagConstraints.WEST;
		gbc_lblCapStartTime.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapStartTime.gridx = 0;
		gbc_lblCapStartTime.gridy = 10;
		pnlCard.add(lblCapStartTime, gbc_lblCapStartTime);

		GridBagConstraints gbc_lblValueStartTime = new GridBagConstraints();
		gbc_lblValueStartTime.anchor = GridBagConstraints.WEST;
		gbc_lblValueStartTime.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueStartTime.weightx = 1.0;
		gbc_lblValueStartTime.gridx = 1;
		gbc_lblValueStartTime.gridy = 10;
		pnlCard.add(lblValueStartTime, gbc_lblValueStartTime);

		GridBagConstraints gbc_lblCapEndTime = new GridBagConstraints();
		gbc_lblCapEndTime.anchor = GridBagConstraints.WEST;
		gbc_lblCapEndTime.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapEndTime.gridx = 0;
		gbc_lblCapEndTime.gridy = 11;
		pnlCard.add(lblCapEndTime, gbc_lblCapEndTime);

		GridBagConstraints gbc_lblValueEndTime = new GridBagConstraints();
		gbc_lblValueEndTime.anchor = GridBagConstraints.WEST;
		gbc_lblValueEndTime.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueEndTime.weightx = 1.0;
		gbc_lblValueEndTime.gridx = 1;
		gbc_lblValueEndTime.gridy = 11;
		pnlCard.add(lblValueEndTime, gbc_lblValueEndTime);

		GridBagConstraints gbc_lblCapStatus = new GridBagConstraints();
		gbc_lblCapStatus.anchor = GridBagConstraints.WEST;
		gbc_lblCapStatus.insets = new Insets(0, 0, 10, 40);
		gbc_lblCapStatus.gridx = 0;
		gbc_lblCapStatus.gridy = 12;
		pnlCard.add(lblCapStatus, gbc_lblCapStatus);

		GridBagConstraints gbc_lblValueStatus = new GridBagConstraints();
		gbc_lblValueStatus.anchor = GridBagConstraints.WEST;
		gbc_lblValueStatus.insets = new Insets(0, 0, 10, 0);
		gbc_lblValueStatus.weightx = 1.0;
		gbc_lblValueStatus.gridx = 1;
		gbc_lblValueStatus.gridy = 12;
		pnlCard.add(lblValueStatus, gbc_lblValueStatus);

		GridBagConstraints gbc_pnlButtons = new GridBagConstraints();
		gbc_pnlButtons.gridwidth = 2;
		gbc_pnlButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlButtons.anchor = GridBagConstraints.WEST;
		gbc_pnlButtons.insets = new Insets(14, 0, 0, 0);
		gbc_pnlButtons.weightx = 1.0;
		gbc_pnlButtons.gridx = 0;
		gbc_pnlButtons.gridy = 13;
		pnlCard.add(pnlButtons, gbc_pnlButtons);

		btnBackToDashboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleBackToDashboard();
			}
		});

		btnViewReservations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleViewReservations();
			}
		});

	}

	public void setDashboard(StudentDashboard dashboard) {
		this.dashboard = dashboard;
	}

	/** Fills the screen with the details of the reservation that was just created. */
	public void showReservation(Reservation reservation) {
		Student student = reservation.getStudent();
		lblValueReservationId.setText(reservation.getReservationId());
		lblValueStudentName.setText(student.getFullName());
		lblValueStudentId.setText(student.getStudentId());
		lblValueCourse.setText(student.getCourse());
		lblValueYearSection.setText(student.getYearLevel() + " - Section " + student.getSection());
		lblValueLaboratory.setText(reservation.getLaboratoryName());
		lblValueComputer.setText(reservation.getComputer().getComputerNumber());
		lblValueDate.setText(Validator.DATE_FORMAT.format(reservation.getDate()));
		lblValueStartTime.setText(Validator.TIME_FORMAT.format(reservation.getStartTime()));
		lblValueEndTime.setText(Validator.TIME_FORMAT.format(reservation.getEndTime()));
		lblValueStatus.setText(reservation.getStatus().getLabel());
	}

	private void handleBackToDashboard() {
		dashboard.showHome();
	}

	private void handleViewReservations() {
		dashboard.showMyReservations();
	}
}