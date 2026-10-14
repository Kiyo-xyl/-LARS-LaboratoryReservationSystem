package lars.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import lars.controller.ComputerController;
import lars.controller.ReservationController;
import lars.model.AvailabilityStatus;
import lars.model.Computer;

/**
 * Shows the 20 computers of COMLAB or ILAB as colored cards for the chosen date and time.
 * One panel is reused for both laboratories - call setLaboratory("COMLAB") or ("ILAB").
 * Only a status is shown for each computer; other students' identities are never revealed.
 */
public class LaboratoryView extends JPanel {

	private static final long serialVersionUID = 1L;

	private StudentDashboard dashboard; // set with setDashboard() so this panel can be edited in WindowBuilder
	private String laboratoryName;
	private final ComputerController computerController = new ComputerController();
	private final ReservationController reservationController = new ReservationController();

	private JPanel pnlHeader;
	private JPanel pnlTitles;
	private JPanel pnlFilter;
	private JPanel pnlComputers;
	private JPanel pnlLegend;
	private JLabel lblTitle;
	private JLabel lblSubtitle;
	private TimeSlotPicker timeSlotPicker;

	public LaboratoryView() {
		setBackground(Theme.LIGHT_GRAY);
		setBorder(new EmptyBorder(24, 28, 24, 28));
		setLayout(new BorderLayout(0, 16));

		// ---- header: title + date/time picker ----
		pnlHeader = new JPanel();
		pnlHeader.setOpaque(false);
		pnlHeader.setLayout(new BorderLayout(0, 12));
		add(pnlHeader, BorderLayout.NORTH);

		pnlTitles = new JPanel();
		pnlTitles.setOpaque(false);
		pnlTitles.setLayout(new GridLayout(2, 1, 0, 2));
		pnlHeader.add(pnlTitles, BorderLayout.NORTH);

		lblTitle = new JLabel("Laboratory");
		lblTitle.setFont(Theme.FONT_TITLE);
		lblTitle.setForeground(Theme.NAVY);
		pnlTitles.add(lblTitle);

		lblSubtitle = new JLabel("");
		lblSubtitle.setFont(Theme.FONT_BODY);
		lblSubtitle.setForeground(Theme.TEXT_MUTED);
		pnlTitles.add(lblSubtitle);

		pnlFilter = new JPanel();
		pnlFilter.setBackground(Theme.WHITE);
		pnlFilter.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BORDER_GRAY),
				BorderFactory.createEmptyBorder(14, 16, 14, 16)));
		pnlFilter.setLayout(new BorderLayout(0, 0));
		pnlHeader.add(pnlFilter, BorderLayout.CENTER);

		timeSlotPicker = new TimeSlotPicker();
		pnlFilter.add(timeSlotPicker, BorderLayout.CENTER);

		// ---- the 20 computer cards (4 rows x 5 columns) ----
		pnlComputers = new JPanel();
		pnlComputers.setOpaque(false);
		pnlComputers.setLayout(new GridLayout(4, 5, 12, 12));
		add(pnlComputers, BorderLayout.CENTER);

		// ---- legend ----
		pnlLegend = new JPanel();
		pnlLegend.setOpaque(false);
		pnlLegend.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		add(pnlLegend, BorderLayout.SOUTH);
		addLegendItem(Theme.AVAILABLE_GREEN, "Available");
		addLegendItem(Theme.RESERVED_RED, "Reserved for this time");
		addLegendItem(Theme.MAINTENANCE_ORANGE, "Under maintenance");
		addLegendItem(Theme.UNAVAILABLE_GRAY, "Unavailable / Closed");

		// Whenever the date or time changes, redraw the cards.
		timeSlotPicker.addChangeListener(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	public void setDashboard(StudentDashboard dashboard) {
		this.dashboard = dashboard;
	}

	/** Switches this panel to COMLAB or ILAB. */
	public void setLaboratory(String laboratoryName) {
		this.laboratoryName = laboratoryName;
		lblTitle.setText(laboratoryName + " Computers");
		timeSlotPicker.setLaboratory(laboratoryName); // reloads the time dropdowns, which redraws the cards
	}

	/** Redraws the 20 cards from the current in-memory data. */
	public void refresh() {
		if (laboratoryName == null) {
			return;
		}
		LocalDate date = timeSlotPicker.getDate();
		LocalTime start = timeSlotPicker.getStartTime();
		LocalTime end = timeSlotPicker.getEndTime();

		pnlComputers.removeAll();
		for (Computer computer : computerController.getComputersOf(laboratoryName)) {
			AvailabilityStatus status = reservationController.getAvailability(computer, date, start, end);
			pnlComputers.add(createComputerCard(computer, status));
		}
		pnlComputers.revalidate();
		pnlComputers.repaint();
	}

	// ---------------------------------------------------------------- building pieces

	private JButton createComputerCard(final Computer computer, final AvailabilityStatus status) {
		String html = "<html><div style='text-align:center'><span style='font-size:16px'><b>"
				+ computer.getComputerNumber() + "</b></span><br>" + status.getLabel() + "</div></html>";
		JButton card = new JButton(html);
		card.setUI(new BasicButtonUI());   // lets the background color show on every operating system
		card.setOpaque(true);
		card.setFocusPainted(false);
		card.setFont(Theme.FONT_BODY);
		card.setBackground(Theme.colorFor(status));
		card.setForeground(Theme.textColorFor(status));
		card.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
		card.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
		card.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onComputerClicked(computer, status);
			}
		});
		return card;
	}

	private void addLegendItem(Color color, String text) {
		JLabel swatch = new JLabel();
		swatch.setOpaque(true);
		swatch.setBackground(color);
		swatch.setPreferredSize(new Dimension(18, 18));
		pnlLegend.add(swatch);

		JLabel label = new JLabel(text);
		label.setFont(Theme.FONT_SMALL);
		label.setForeground(Theme.TEXT_DARK);
		pnlLegend.add(label);
		pnlLegend.add(javax.swing.Box.createHorizontalStrut(10));
	}

	// ---------------------------------------------------------------- click handling

	/** Available -> open the reservation form. Anything else -> explain why it cannot be reserved. */
	private void onComputerClicked(Computer computer, AvailabilityStatus status) {
		String name = computer.getLaboratoryName() + " " + computer.getComputerNumber();
		if (status == AvailabilityStatus.AVAILABLE) {
			dashboard.showReservationForm(computer, timeSlotPicker.getDate(),
					timeSlotPicker.getStartTime(), timeSlotPicker.getEndTime());
			return;
		}
		String message;
		switch (status) {
			case RESERVED:
				message = name + " is already reserved for the selected time.\nChoose another time or another computer.";
				break;
			case MAINTENANCE:
				message = name + " is under maintenance and cannot be reserved.";
				break;
			case UNAVAILABLE:
				message = name + " has been marked unavailable by the administrator.";
				break;
			default:
				message = name + " cannot be reserved for the selected date and time.\n"
						+ "Check the opening hours shown above. Weekends are always closed and past times cannot be booked.";
				break;
		}
		JOptionPane.showMessageDialog(this, message, "Computer Not Available", JOptionPane.INFORMATION_MESSAGE);
	}
}