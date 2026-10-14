package lars.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lars.controller.ComputerController;
import lars.controller.ReservationController;
import lars.data.LARSData;
import lars.model.AvailabilityStatus;

/**
 * The student's Home page: statistics and quick-access buttons.
 * Every number is calculated from the real in-memory data each time refresh() runs.
 */
public class StudentHomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private StudentDashboard dashboard;
	private final ComputerController computerController = new ComputerController();
	private final ReservationController reservationController = new ReservationController();

	private JPanel pnlTitles;
	private JPanel pnlStats;
	private JPanel pnlQuickAccess;
	private JPanel pnlQuickButtons;
	private JLabel lblWelcome;
	private JLabel lblSubtitle;
	private JLabel lblTotalValue;
	private JLabel lblAvailableValue;
	private JLabel lblReservedValue;
	private JLabel lblMyReservationsValue;
	private JLabel lblQuickTitle;
	private JLabel lblQuickNote;
	private JButton btnOpenComlab;
	private JButton btnOpenIlab;

	public StudentHomePanel() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(28, 32, 28, 32));
		setLayout(new BorderLayout(0, 24));

		// ---- titles ----
		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 4));
		add(pnlTitles, BorderLayout.NORTH);

		lblWelcome = new JLabel("Welcome!");
		lblWelcome.setFont(Theme.FONT_TITLE);
		lblWelcome.setForeground(Theme.NAVY);
		pnlTitles.add(lblWelcome);

		lblSubtitle = new JLabel("Welcome CCC Students!");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);

		// ---- statistic cards ----
		pnlStats = new JPanel();
		pnlStats.setOpaque(false);
		pnlStats.setLayout(new GridLayout(1, 4, 16, 0));

		lblTotalValue = new JLabel("0");
		lblAvailableValue = new JLabel("0");
		lblReservedValue = new JLabel("0");
		lblMyReservationsValue = new JLabel("0");
		pnlStats.add(createStatCard("Total Computers", lblTotalValue, Theme.PRIMARY));
		pnlStats.add(createStatCard("Available Now", lblAvailableValue, Theme.AVAILABLE_GREEN));
		pnlStats.add(createStatCard("Reserved Now", lblReservedValue, Theme.RESERVED_RED));
		pnlStats.add(createStatCard("My Reservations", lblMyReservationsValue, Theme.NAVY));

		// ---- quick access ----
		pnlQuickAccess = new JPanel();
		pnlQuickAccess.setBackground(Theme.WHITE);
		pnlQuickAccess.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(20, 24, 20, 24)));
		pnlQuickAccess.setLayout(new BorderLayout(0, 14));

		lblQuickTitle = new JLabel("Quick Access");
		lblQuickTitle.setFont(Theme.FONT_HEADING);
		lblQuickTitle.setForeground(Theme.NAVY);
		pnlQuickAccess.add(lblQuickTitle, BorderLayout.NORTH);

		pnlQuickButtons = new JPanel();
		pnlQuickButtons.setBackground(Theme.WHITE);
		pnlQuickButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
		pnlQuickAccess.add(pnlQuickButtons, BorderLayout.CENTER);

		btnOpenComlab = new JButton("Open COMLAB");
		Theme.stylePrimaryButton(btnOpenComlab);
		pnlQuickButtons.add(btnOpenComlab);

		btnOpenIlab = new JButton("Open ILAB");
		Theme.stylePrimaryButton(btnOpenIlab);
		pnlQuickButtons.add(btnOpenIlab);

		lblQuickNote = new JLabel("Counts show the situation right now. A computer can be free later even if it is reserved now.");
		lblQuickNote.setFont(Theme.FONT_SMALL);
		lblQuickNote.setForeground(Theme.TEXT_MUTED);
		pnlQuickAccess.add(lblQuickNote, BorderLayout.SOUTH);

		// stats on top of quick access, both placed in the centre area
		JPanel pnlCenter = new JPanel();
		pnlCenter.setOpaque(false);
		pnlCenter.setLayout(new BorderLayout(0, 24));
		pnlCenter.add(pnlStats, BorderLayout.NORTH);
		pnlCenter.add(pnlQuickAccess, BorderLayout.CENTER);
		add(pnlCenter, BorderLayout.CENTER);

		btnOpenComlab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dashboard.showLaboratory("COMLAB");
			}
		});
		btnOpenIlab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dashboard.showLaboratory("ILAB");
			}
		});
	}

	public void setDashboard(StudentDashboard dashboard) {
		this.dashboard = dashboard;
	}

	/** Recalculates every number from the shared in-memory data. */
	public void refresh() {
		lblWelcome.setText("Welcome, " + LARSData.getInstance().requireStudent().getFullName() + "!");
		lblTotalValue.setText(String.valueOf(computerController.countTotal(null)));
		lblAvailableValue.setText(String.valueOf(computerController.countNow(null, AvailabilityStatus.AVAILABLE)));
		lblReservedValue.setText(String.valueOf(computerController.countNow(null, AvailabilityStatus.RESERVED)));
		lblMyReservationsValue.setText(String.valueOf(reservationController.countMyReservations()));
	}

	/** One white card with a colored left stripe, a small title and a big number. */
	private JPanel createStatCard(String title, JLabel valueLabel, Color accent) {
		JPanel card = new JPanel();
		card.setBackground(Theme.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 6, 0, 0, accent),
				BorderFactory.createEmptyBorder(18, 20, 18, 20)));
		card.setLayout(new BorderLayout(0, 6));

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(Theme.FONT_LABEL);
		lblTitle.setForeground(Theme.TEXT_MUTED);
		card.add(lblTitle, BorderLayout.NORTH);

		valueLabel.setFont(Theme.FONT_TITLE.deriveFont(40f));
		valueLabel.setForeground(Theme.TEXT_DARK);
		card.add(valueLabel, BorderLayout.CENTER);
		return card;
	}
}