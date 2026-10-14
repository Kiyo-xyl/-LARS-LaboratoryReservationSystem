package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import lars.controller.ReservationController;
import lars.controller.Validator;
import lars.model.Reservation;

/**
 * The logged-in student's own reservations in a JTable.
 * Select a row, then use "View Details" or "Cancel Reservation".
 * ReservationController only ever returns THIS student's reservations.
 */
public class MyReservationsForm extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ReservationController reservationController = new ReservationController();
	private List<Reservation> shownReservations = new ArrayList<Reservation>(); // same order as the table rows

	private JPanel pnlTitles;
	private JPanel pnlButtons;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private JScrollPane scrollPane;
	private JTable tblReservations;
	private DefaultTableModel tableModel;
	private JButton btnViewDetails;
	private JButton btnCancelReservation;

	public MyReservationsForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 16));

		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 2));
		add(pnlTitles, BorderLayout.NORTH);

		lblTitle = new JLabel("My Reservations");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);

		lblSubtitle = new JLabel("Select a reservation, then view its details or cancel it.");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);

		// ---- table ----
		String[] columns = {"Reservation ID", "Laboratory", "Computer", "Date", "Start Time", "End Time", "Status"};
		tableModel = new DefaultTableModel(new Object[][] {}, columns) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // the table is for viewing only
			}
		};
		tblReservations = new JTable(tableModel);
		Theme.styleTable(tblReservations);
		tblReservations.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tblReservations.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

		scrollPane = new JScrollPane(tblReservations);
		scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_GRAY));
		scrollPane.getViewport().setBackground(Theme.WHITE);
		add(scrollPane, BorderLayout.CENTER);

		// ---- buttons ----
		pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
		add(pnlButtons, BorderLayout.SOUTH);

		btnViewDetails = new JButton("View Details");
		Theme.styleSecondaryButton(btnViewDetails);
		pnlButtons.add(btnViewDetails);

		btnCancelReservation = new JButton("Cancel Reservation");
		Theme.stylePrimaryButton(btnCancelReservation);
		pnlButtons.add(btnCancelReservation);

		btnViewDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleViewDetails();
			}
		});
		btnCancelReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}
		});
	}

	/** Reloads the table from the shared data and keeps the selected row if it still exists. */
	public void refresh() {
		Reservation previouslySelected = getSelectedReservation();
		shownReservations = reservationController.getMyReservations();

		tableModel.setRowCount(0);
		for (Reservation reservation : shownReservations) {
			tableModel.addRow(new Object[] {
				reservation.getReservationId(),
				reservation.getLaboratoryName(),
				reservation.getComputer().getComputerNumber(),
				Validator.DATE_FORMAT.format(reservation.getDate()),
				Validator.TIME_FORMAT.format(reservation.getStartTime()),
				Validator.TIME_FORMAT.format(reservation.getEndTime()),
				reservation.getStatus().getLabel()
			});
		}
		if (previouslySelected != null) {
			int row = shownReservations.indexOf(previouslySelected);
			if (row >= 0) {
				tblReservations.setRowSelectionInterval(row, row);
			}
		}
	}

	private Reservation getSelectedReservation() {
		int row = tblReservations.getSelectedRow();
		if (row < 0 || row >= shownReservations.size()) {
			return null;
		}
		return shownReservations.get(row);
	}

	private String describe(Reservation r) {
		return r.getLaboratoryName() + " " + r.getComputer().getComputerNumber() + " on "
				+ Validator.DATE_FORMAT.format(r.getDate()) + ", "
				+ Validator.TIME_FORMAT.format(r.getStartTime()) + " - "
				+ Validator.TIME_FORMAT.format(r.getEndTime());
	}

	private void handleViewDetails() {
		Reservation reservation = getSelectedReservation();
		if (reservation == null) {
			JOptionPane.showMessageDialog(this, "Please select a reservation first.",
					"No Reservation Selected", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String details = "Reservation ID: " + reservation.getReservationId()
				+ "\nLaboratory: " + reservation.getLaboratoryName()
				+ "\nComputer: " + reservation.getComputer().getComputerNumber()
				+ "\nDate: " + Validator.DATE_FORMAT.format(reservation.getDate())
				+ "\nTime: " + Validator.TIME_FORMAT.format(reservation.getStartTime())
				+ " - " + Validator.TIME_FORMAT.format(reservation.getEndTime())
				+ "\nStatus: " + reservation.getStatus().getLabel();
		JOptionPane.showMessageDialog(this, details, "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Asks for confirmation, then lets the controller cancel (the controller also checks ownership). */
	private void handleCancel() {
		Reservation reservation = getSelectedReservation();
		if (reservation == null) {
			JOptionPane.showMessageDialog(this, "Please select a reservation first.",
					"No Reservation Selected", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this,
				"Cancel reservation " + reservation.getReservationId() + "?\n" + describe(reservation),
				"Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			reservationController.cancelMyReservation(reservation);
			JOptionPane.showMessageDialog(this,
					"Reservation cancelled. The computer is available again for that time.",
					"Reservation Cancelled", JOptionPane.INFORMATION_MESSAGE);
			refresh();
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot Cancel", JOptionPane.WARNING_MESSAGE);
		}
	}
}