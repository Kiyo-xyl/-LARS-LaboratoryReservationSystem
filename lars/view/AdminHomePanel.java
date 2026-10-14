package lars.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lars.controller.AdminController;
import lars.controller.ComputerController;
import lars.model.AvailabilityStatus;

/** The administrator's dashboard page. Every number comes from the live in-memory data. */
public class AdminHomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ComputerController computerController = new ComputerController();
	private final AdminController adminController = new AdminController();

	private JPanel pnlTitles;
	private JPanel pnlStats;
	private JPanel pnlSummaries;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private JLabel lblTotalValue;
	private JLabel lblAvailableValue;
	private JLabel lblReservedValue;
	private JLabel lblMaintenanceValue;
	private JLabel lblReservationsValue;
	private JLabel lblComlabSummary;
	private JLabel lblIlabSummary;

	public AdminHomePanel() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(28, 32, 28, 32));
		setLayout(new BorderLayout(0, 24));

		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 4));
		add(pnlTitles, BorderLayout.NORTH);

		lblTitle = new JLabel("Administrator Dashboard");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);

		lblSubtitle = new JLabel("welcome to CCC Administrator ");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);

		// ---- five statistic cards ----
		pnlStats = new JPanel();
		pnlStats.setOpaque(false);
		pnlStats.setLayout(new GridLayout(1, 5, 12, 0));
		lblTotalValue = new JLabel("0");
		lblAvailableValue = new JLabel("0");
		lblReservedValue = new JLabel("0");
		lblMaintenanceValue = new JLabel("0");
		lblReservationsValue = new JLabel("0");
		pnlStats.add(createStatCard("Total Computers", lblTotalValue, Theme.PRIMARY));
		pnlStats.add(createStatCard("Available Now", lblAvailableValue, Theme.AVAILABLE_GREEN));
		pnlStats.add(createStatCard("Reserved Now", lblReservedValue, Theme.RESERVED_RED));
		pnlStats.add(createStatCard("Maintenance", lblMaintenanceValue, Theme.MAINTENANCE_ORANGE));
		pnlStats.add(createStatCard("Total Reservations", lblReservationsValue, Theme.NAVY));

		// ---- one summary box per laboratory ----
		pnlSummaries = new JPanel();
		pnlSummaries.setOpaque(false);
		pnlSummaries.setLayout(new GridLayout(1, 2, 16, 0));
		lblComlabSummary = new JLabel(" ");
		lblIlabSummary = new JLabel(" ");
		pnlSummaries.add(createSummaryBox("COMLAB Summary", lblComlabSummary));
		pnlSummaries.add(createSummaryBox("ILAB Summary", lblIlabSummary));

		JPanel pnlBelow = new JPanel();
		pnlBelow.setOpaque(false);
		pnlBelow.setLayout(new BorderLayout(0, 0));
		pnlBelow.add(pnlSummaries, BorderLayout.NORTH);

		JPanel pnlCenter = new JPanel();
		pnlCenter.setOpaque(false);
		pnlCenter.setLayout(new BorderLayout(0, 24));
		pnlCenter.add(pnlStats, BorderLayout.NORTH);
		pnlCenter.add(pnlBelow, BorderLayout.CENTER);
		add(pnlCenter, BorderLayout.CENTER);
	}

	/** Recalculates every number from the shared in-memory data. */
	public void refresh() {
		lblTotalValue.setText(String.valueOf(computerController.countTotal(null)));
		lblAvailableValue.setText(String.valueOf(computerController.countNow(null, AvailabilityStatus.AVAILABLE)));
		lblReservedValue.setText(String.valueOf(computerController.countNow(null, AvailabilityStatus.RESERVED)));
		lblMaintenanceValue.setText(String.valueOf(computerController.countNow(null, AvailabilityStatus.MAINTENANCE)));
		lblReservationsValue.setText(String.valueOf(adminController.countAllReservations()));
		lblComlabSummary.setText(buildSummary("COMLAB"));
		lblIlabSummary.setText(buildSummary("ILAB"));
	}

	private String buildSummary(String laboratoryName) {
		return "<html>Total computers: " + computerController.countTotal(laboratoryName)
				+ "<br>Available now: " + computerController.countNow(laboratoryName, AvailabilityStatus.AVAILABLE)
				+ "<br>Reserved now: " + computerController.countNow(laboratoryName, AvailabilityStatus.RESERVED)
				+ "<br>Under maintenance: " + computerController.countNow(laboratoryName, AvailabilityStatus.MAINTENANCE)
				+ "<br>Unavailable: " + computerController.countNow(laboratoryName, AvailabilityStatus.UNAVAILABLE)
				+ "<br>Upcoming reservations: " + adminController.countUpcomingReservations(laboratoryName)
				+ "</html>";
	}

	private JPanel createStatCard(String title, JLabel valueLabel, Color accent) {
		JPanel card = new JPanel();
		card.setBackground(Theme.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 6, 0, 0, accent),
				BorderFactory.createEmptyBorder(16, 14, 16, 6)));
		card.setLayout(new BorderLayout(0, 6));

		JLabel lblCardTitle = new JLabel(title);
		lblCardTitle.setFont(Theme.FONT_SMALL.deriveFont(Font.BOLD)); // slightly smaller so long titles fit
		lblCardTitle.setForeground(Theme.TEXT_MUTED);
		card.add(lblCardTitle, BorderLayout.NORTH);

		valueLabel.setFont(Theme.FONT_TITLE.deriveFont(36f));
		valueLabel.setForeground(Theme.TEXT_DARK);
		card.add(valueLabel, BorderLayout.CENTER);
		return card;
	}

	private JPanel createSummaryBox(String title, JLabel body) {
		JPanel box = new JPanel();
		box.setBackground(Theme.WHITE);
		box.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(18, 22, 18, 22)));
		box.setLayout(new BorderLayout(0, 10));

		JLabel lblBoxTitle = new JLabel(title);
		lblBoxTitle.setFont(Theme.FONT_HEADING);
		lblBoxTitle.setForeground(Theme.NAVY);
		box.add(lblBoxTitle, BorderLayout.NORTH);

		body.setFont(Theme.FONT_BODY);
		body.setForeground(Theme.TEXT_DARK);
		box.add(body, BorderLayout.CENTER);
		return box;
	}
}