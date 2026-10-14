package lars.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import lars.controller.AdminController;
import lars.controller.Validator;
import lars.model.Reservation;
import lars.model.ReservationStatus;

/** All reservations from both laboratories, with search, filters and cancellation (administrators only). */
public class ReservationManagementForm extends JPanel {

	private static final long serialVersionUID = 1L;

	private final AdminController adminController = new AdminController();
	private List<Reservation> shownReservations = new ArrayList<Reservation>(); // same order as the table rows

	private JPanel pnlTop;
	private JPanel pnlTitles;
	private JPanel pnlFilter;
	private JPanel pnlFilterRow1;
	private JPanel pnlFilterRow2;
	private JPanel pnlBottom;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private JLabel lblSearchName;
	private JLabel lblSearchStudentId;
	private JLabel lblLaboratory;
	private JLabel lblStatus;
	private JLabel lblCount;
	private JTextField txtSearchName;
	private JTextField txtSearchStudentId;
	private JComboBox<String> cmbLaboratoryFilter;
	private JComboBox<String> cmbStatusFilter;
	private JCheckBox chkFilterDate;
	private JSpinner spnFilterDate;
	private JButton btnSearch;
	private JButton btnClear;
	private JScrollPane scrollPane;
	private JTable tblReservations;
	private DefaultTableModel tableModel;
	private JButton btnCancelReservation;

	public ReservationManagementForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 14));

		pnlTop = new JPanel();
		pnlTop.setOpaque(false);
		pnlTop.setLayout(new BorderLayout(0, 12));
		add(pnlTop, BorderLayout.NORTH);

		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 2));
		lblTitle = new JLabel("Reservation Management");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);
		lblSubtitle = new JLabel("Search, filter and cancel reservations from COMLAB and ILAB.");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);
		pnlTop.add(pnlTitles, BorderLayout.NORTH);

		// ---- filters (two rows) ----
		pnlFilter = new JPanel();
		pnlFilter.setBackground(Theme.WHITE);
		pnlFilter.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(12, 14, 12, 14)));
		pnlFilter.setLayout(new GridLayout(2, 1, 0, 10));
		pnlTop.add(pnlFilter, BorderLayout.CENTER);

		pnlFilterRow1 = new JPanel();
		pnlFilterRow1.setBackground(Theme.WHITE);
		pnlFilterRow1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnlFilter.add(pnlFilterRow1);

		lblSearchName = createFilterLabel("Student Name");
		pnlFilterRow1.add(lblSearchName);
		txtSearchName = new JTextField();
		txtSearchName.setColumns(13);
		Theme.styleTextField(txtSearchName);
		pnlFilterRow1.add(txtSearchName);

		lblSearchStudentId = createFilterLabel("Student ID");
		pnlFilterRow1.add(lblSearchStudentId);
		txtSearchStudentId = new JTextField();
		txtSearchStudentId.setColumns(9);
		Theme.styleTextField(txtSearchStudentId);
		pnlFilterRow1.add(txtSearchStudentId);

		lblLaboratory = createFilterLabel("Laboratory");
		pnlFilterRow1.add(lblLaboratory);
		cmbLaboratoryFilter = new JComboBox<String>(new String[] {"All", "COMLAB", "ILAB"});
		Theme.styleComboBox(cmbLaboratoryFilter);
		pnlFilterRow1.add(cmbLaboratoryFilter);

		pnlFilterRow2 = new JPanel();
		pnlFilterRow2.setBackground(Theme.WHITE);
		pnlFilterRow2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnlFilter.add(pnlFilterRow2);

		chkFilterDate = new JCheckBox("Reservation Date");
		chkFilterDate.setFont(Theme.FONT_LABEL);
		chkFilterDate.setBackground(Theme.WHITE);
		chkFilterDate.setForeground(Theme.TEXT_DARK);
		pnlFilterRow2.add(chkFilterDate);
		spnFilterDate = new JSpinner(new SpinnerDateModel());
		spnFilterDate.setEditor(new JSpinner.DateEditor(spnFilterDate, "MMM dd, yyyy"));
		spnFilterDate.setFont(Theme.FONT_BODY);
		spnFilterDate.setEnabled(false); // only used when the checkbox is ticked
		pnlFilterRow2.add(spnFilterDate);

		lblStatus = createFilterLabel("Status");
		pnlFilterRow2.add(lblStatus);
		cmbStatusFilter = new JComboBox<String>(new String[] {"All", "Upcoming", "Completed", "Cancelled"});
		Theme.styleComboBox(cmbStatusFilter);
		pnlFilterRow2.add(cmbStatusFilter);

		btnSearch = new JButton("Search");
		Theme.stylePrimaryButton(btnSearch);
		pnlFilterRow2.add(btnSearch);
		btnClear = new JButton("Clear Filters");
		Theme.styleSecondaryButton(btnClear);
		pnlFilterRow2.add(btnClear);

		// ---- table (wide, so it scrolls sideways if needed) ----
		String[] columns = {"Reservation ID", "Student Name", "Student ID", "Program / Course", "Year Level",
			"Section", "Laboratory", "Computer", "Date", "Start Time", "End Time", "Status"};
		tableModel = new DefaultTableModel(new Object[][] {}, columns) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblReservations = new JTable(tableModel);
		Theme.styleTable(tblReservations);
		tblReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblReservations.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int[] widths = {105, 160, 95, 210, 85, 70, 90, 85, 105, 90, 90, 90};
		for (int i = 0; i < widths.length; i++) {
			tblReservations.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}
		tblReservations.getColumnModel().getColumn(11).setCellRenderer(new StatusCellRenderer());

		scrollPane = new JScrollPane(tblReservations);
		scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_GRAY));
		scrollPane.getViewport().setBackground(Theme.WHITE);
		add(scrollPane, BorderLayout.CENTER);

		// ---- bottom: cancel button + count ----
		pnlBottom = new JPanel();
		pnlBottom.setOpaque(false);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		btnCancelReservation = new JButton("Cancel Selected Reservation");
		Theme.stylePrimaryButton(btnCancelReservation);
		JPanel pnlButtonHolder = new JPanel();
		pnlButtonHolder.setOpaque(false);
		pnlButtonHolder.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlButtonHolder.add(btnCancelReservation);
		pnlBottom.add(pnlButtonHolder, BorderLayout.WEST);
		lblCount = new JLabel(" ");
		lblCount.setFont(Theme.FONT_BODY);
		lblCount.setForeground(Theme.TEXT_MUTED);
		pnlBottom.add(lblCount, BorderLayout.EAST);
		add(pnlBottom, BorderLayout.SOUTH);

		// ---- events ----
		ActionListener searchAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		};
		btnSearch.addActionListener(searchAction);
		txtSearchName.addActionListener(searchAction);       // pressing Enter also searches
		txtSearchStudentId.addActionListener(searchAction);
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearFilters();
			}
		});
		chkFilterDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spnFilterDate.setEnabled(chkFilterDate.isSelected());
				refresh();
			}
		});
		btnCancelReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}
		});
	}

	private JLabel createFilterLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Theme.FONT_LABEL);
		label.setForeground(Theme.TEXT_DARK);
		return label;
	}

	/** Re-applies the filters currently typed in (so open screens stay filtered when data changes). */
	public void refresh() {
		Reservation previouslySelected = getSelectedReservation();

		LocalDate date = null;
		if (chkFilterDate.isSelected()) {
			date = DateUtil.toLocalDate((Date) spnFilterDate.getValue());
		}
		// The status dropdown order (All, Upcoming, Completed, Cancelled) matches the enum order after "All".
		ReservationStatus status = null;
		if (cmbStatusFilter.getSelectedIndex() > 0) {
			status = ReservationStatus.values()[cmbStatusFilter.getSelectedIndex() - 1];
		}

		shownReservations = adminController.searchReservations(txtSearchName.getText(),
				txtSearchStudentId.getText(), (String) cmbLaboratoryFilter.getSelectedItem(), date, status);

		tableModel.setRowCount(0);
		for (Reservation r : shownReservations) {
			tableModel.addRow(new Object[] {
				r.getReservationId(),
				r.getStudent().getFullName(),
				r.getStudent().getStudentId(),
				r.getStudent().getCourse(),
				r.getStudent().getYearLevel(),
				r.getStudent().getSection(),
				r.getLaboratoryName(),
				r.getComputer().getComputerNumber(),
				Validator.DATE_FORMAT.format(r.getDate()),
				Validator.TIME_FORMAT.format(r.getStartTime()),
				Validator.TIME_FORMAT.format(r.getEndTime()),
				r.getStatus().getLabel()
			});
		}
		lblCount.setText("Showing " + shownReservations.size() + " of "
				+ adminController.countAllReservations() + " reservations");

		if (previouslySelected != null) {
			int row = shownReservations.indexOf(previouslySelected);
			if (row >= 0) {
				tblReservations.setRowSelectionInterval(row, row);
			}
		}
	}

	private void clearFilters() {
		txtSearchName.setText("");
		txtSearchStudentId.setText("");
		cmbLaboratoryFilter.setSelectedIndex(0);
		cmbStatusFilter.setSelectedIndex(0);
		chkFilterDate.setSelected(false);
		spnFilterDate.setEnabled(false);
		refresh();
	}

	private Reservation getSelectedReservation() {
		int row = tblReservations.getSelectedRow();
		if (row < 0 || row >= shownReservations.size()) {
			return null;
		}
		return shownReservations.get(row);
	}

	/** Asks for confirmation, then lets AdminController cancel the reservation. */
	private void handleCancel() {
		Reservation reservation = getSelectedReservation();
		if (reservation == null) {
			JOptionPane.showMessageDialog(this, "Please select a reservation in the table first.",
					"No Reservation Selected", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int answer = JOptionPane.showConfirmDialog(this,
				"Cancel reservation " + reservation.getReservationId() + " for "
						+ reservation.getStudent().getFullName() + "?\n"
						+ reservation.getLaboratoryName() + " " + reservation.getComputer().getComputerNumber()
						+ " on " + Validator.DATE_FORMAT.format(reservation.getDate()) + ", "
						+ Validator.TIME_FORMAT.format(reservation.getStartTime()) + " - "
						+ Validator.TIME_FORMAT.format(reservation.getEndTime()),
				"Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			adminController.cancelReservation(reservation);
			JOptionPane.showMessageDialog(this, "Reservation cancelled. The computer is free again for that time.",
					"Reservation Cancelled", JOptionPane.INFORMATION_MESSAGE);
			refresh();
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot Cancel", JOptionPane.WARNING_MESSAGE);
		}
	}
}