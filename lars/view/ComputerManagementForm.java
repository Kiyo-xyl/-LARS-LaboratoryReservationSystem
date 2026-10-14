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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import lars.controller.ComputerController;
import lars.model.Computer;
import lars.model.ComputerStatus;

/**
 * Administrators see all 40 computers and change a computer's administrative status.
 * "Right Now" shows whether it is reserved at this moment - that is separate from the status.
 * Changing a status never deletes reservations; the admin is warned about affected ones.
 */
public class ComputerManagementForm extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ComputerController computerController = new ComputerController();
	private List<Computer> shownComputers = new ArrayList<Computer>(); // same order as the table rows

	private JPanel pnlTitles;
	private JPanel pnlToolbar;
	private JPanel pnlFilter;
	private JPanel pnlAction;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private JLabel lblFilter;
	private JLabel lblNewStatus;
	private JComboBox<String> cmbLaboratoryFilter;
	private JComboBox<ComputerStatus> cmbNewStatus;
	private JScrollPane scrollPane;
	private JTable tblComputers;
	private DefaultTableModel tableModel;
	private JButton btnUpdateStatus;

	public ComputerManagementForm() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 14));

		// ---- titles + laboratory filter ----
		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 2));
		lblTitle = new JLabel("Computer Management");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);
		lblSubtitle = new JLabel("Select a computer, choose a new status and click Update Status.");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);

		pnlToolbar = new JPanel();
		pnlToolbar.setOpaque(false);
		pnlToolbar.setLayout(new BorderLayout(0, 10));
		pnlToolbar.add(pnlTitles, BorderLayout.NORTH);

		pnlFilter = new JPanel();
		pnlFilter.setOpaque(false);
		pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		lblFilter = new JLabel("Laboratory");
		lblFilter.setFont(Theme.FONT_LABEL);
		lblFilter.setForeground(Theme.TEXT_DARK);
		pnlFilter.add(lblFilter);
		cmbLaboratoryFilter = new JComboBox<String>(new String[] {"All", "COMLAB", "ILAB"});
		Theme.styleComboBox(cmbLaboratoryFilter);
		pnlFilter.add(cmbLaboratoryFilter);
		pnlToolbar.add(pnlFilter, BorderLayout.CENTER);
		add(pnlToolbar, BorderLayout.NORTH);

		// ---- table ----
		String[] columns = {"Laboratory", "Computer", "Administrative Status", "Right Now", "Upcoming Reservations"};
		tableModel = new DefaultTableModel(new Object[][] {}, columns) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblComputers = new JTable(tableModel);
		Theme.styleTable(tblComputers);
		tblComputers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblComputers.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());
		tblComputers.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

		scrollPane = new JScrollPane(tblComputers);
		scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_GRAY));
		scrollPane.getViewport().setBackground(Theme.WHITE);
		add(scrollPane, BorderLayout.CENTER);

		// ---- change status ----
		pnlAction = new JPanel();
		pnlAction.setOpaque(false);
		pnlAction.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		lblNewStatus = new JLabel("Set selected computer to");
		lblNewStatus.setFont(Theme.FONT_LABEL);
		lblNewStatus.setForeground(Theme.TEXT_DARK);
		pnlAction.add(lblNewStatus);
		cmbNewStatus = new JComboBox<ComputerStatus>(ComputerStatus.values());
		cmbNewStatus.setFont(Theme.FONT_BODY);
		cmbNewStatus.setBackground(Theme.WHITE);
		pnlAction.add(cmbNewStatus);
		btnUpdateStatus = new JButton("Update Status");
		Theme.stylePrimaryButton(btnUpdateStatus);
		pnlAction.add(btnUpdateStatus);
		add(pnlAction, BorderLayout.SOUTH);

		cmbLaboratoryFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btnUpdateStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleUpdateStatus();
			}
		});
	}

	/** Reloads the table (keeps the selected computer and the laboratory filter). */
	public void refresh() {
		Computer previouslySelected = getSelectedComputer();
		String filter = (String) cmbLaboratoryFilter.getSelectedItem();

		shownComputers = new ArrayList<Computer>();
		for (Computer computer : computerController.getAllComputers()) {
			if (filter == null || filter.equals("All") || computer.getLaboratoryName().equals(filter)) {
				shownComputers.add(computer);
			}
		}

		tableModel.setRowCount(0);
		for (Computer computer : shownComputers) {
			tableModel.addRow(new Object[] {
				computer.getLaboratoryName(),
				computer.getComputerNumber(),
				computer.getStatus().getLabel(),
				computerController.getStatusNow(computer).getLabel(),
				computerController.countUpcomingReservations(computer)
			});
		}
		if (previouslySelected != null) {
			int row = shownComputers.indexOf(previouslySelected);
			if (row >= 0) {
				tblComputers.setRowSelectionInterval(row, row);
			}
		}
	}

	private Computer getSelectedComputer() {
		int row = tblComputers.getSelectedRow();
		if (row < 0 || row >= shownComputers.size()) {
			return null;
		}
		return shownComputers.get(row);
	}

	/** Warns about affected reservations, asks for confirmation, then lets the controller change the status. */
	private void handleUpdateStatus() {
		Computer computer = getSelectedComputer();
		if (computer == null) {
			JOptionPane.showMessageDialog(this, "Please select a computer in the table first.",
					"No Computer Selected", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		ComputerStatus newStatus = (ComputerStatus) cmbNewStatus.getSelectedItem();
		String name = computer.getLaboratoryName() + " " + computer.getComputerNumber();
		if (computer.getStatus() == newStatus) {
			JOptionPane.showMessageDialog(this, name + " is already set to " + newStatus.getLabel() + ".",
					"No Change", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int affected = 0;
		if (newStatus != ComputerStatus.AVAILABLE) {
			affected = computerController.countUpcomingReservations(computer);
		}
		String question = "Change " + name + " from " + computer.getStatus().getLabel()
				+ " to " + newStatus.getLabel() + "?";
		if (affected > 0) {
			question += "\n\nWarning: this computer has " + affected + " upcoming reservation(s)."
					+ "\nThey will NOT be deleted, but students may not be able to use the computer."
					+ "\nReview or cancel them in Reservation Management.";
		}
		int answer = JOptionPane.showConfirmDialog(this, question, "Confirm Status Change",
				JOptionPane.YES_NO_OPTION, affected > 0 ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			computerController.setComputerStatus(computer, newStatus);
			JOptionPane.showMessageDialog(this, name + " is now " + newStatus.getLabel() + ".",
					"Status Updated", JOptionPane.INFORMATION_MESSAGE);
			refresh();
		} catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Status Not Changed", JOptionPane.WARNING_MESSAGE);
		}
	}
}