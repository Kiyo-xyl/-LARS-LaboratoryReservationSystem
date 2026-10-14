package lars.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import lars.model.AvailabilityStatus;

/**
 * The LARS look in one place: colors, fonts and small styling helpers.
 * Change a color here and every screen changes with it.
 */
public final class Theme {

	// ----- colors -----
	public static final Color NAVY = new Color(0x10, 0x21, 0x3F);          // sidebar / branding
	public static final Color NAVY_LIGHT = new Color(0x1E, 0x3A, 0x6E);    // hover on the sidebar
	public static final Color PRIMARY = new Color(0x25, 0x63, 0xEB);       // blue buttons
	public static final Color PRIMARY_DARK = new Color(0x1D, 0x4E, 0xD8);  // blue button on hover
	public static final Color WHITE = Color.WHITE;
	public static final Color LIGHT_GRAY = new Color(0xF3, 0xF4, 0xF6);    // content background
	public static final Color BORDER_GRAY = new Color(0xD1, 0xD5, 0xDB);
	public static final Color TEXT_DARK = new Color(0x1F, 0x29, 0x37);
	public static final Color TEXT_MUTED = new Color(0x6B, 0x72, 0x80);

	// ----- computer status colors -----
	public static final Color AVAILABLE_GREEN = new Color(0x22, 0xC5, 0x5E);
	public static final Color RESERVED_RED = new Color(0xEF, 0x44, 0x44);
	public static final Color UNAVAILABLE_GRAY = new Color(0x9C, 0xA3, 0xAF);
	public static final Color MAINTENANCE_ORANGE = new Color(0xF5, 0x9E, 0x0B);

	// ----- fonts ("Segoe UI" on Windows; Java picks a similar font elsewhere) -----
	public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
	public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
	public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
	public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
	public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
	public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

	private Theme() {
		// utility class
	}

	/** Solid blue button with white text. */
	public static void stylePrimaryButton(final JButton button) {
		flatten(button);
		button.setBackground(PRIMARY);
		button.setForeground(WHITE);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (button.isEnabled()) {
					button.setBackground(PRIMARY_DARK);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (button.isEnabled()) {
					button.setBackground(PRIMARY);
				}
			}
		});
	}

	/** White button with a gray border (for Back / Cancel). */
	public static void styleSecondaryButton(JButton button) {
		flatten(button);
		button.setBackground(WHITE);
		button.setForeground(TEXT_DARK);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_GRAY),
				BorderFactory.createEmptyBorder(9, 19, 9, 19)));
	}

	/** A button that looks like a blue text link (for "Sign Up", "Back to Login"). */
	public static void styleLinkButton(JButton button) {
		button.setUI(new BasicButtonUI());
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setFont(FONT_BUTTON);
		button.setForeground(PRIMARY);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/** White text box with a thin gray border and inner padding. Works for text and password fields. */
	public static void styleTextField(JTextComponent field) {
		field.setFont(FONT_BODY);
		field.setForeground(TEXT_DARK);
		field.setBackground(WHITE);
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_GRAY),
				BorderFactory.createEmptyBorder(7, 10, 7, 10)));
	}

	public static void styleComboBox(JComboBox<String> combo) {
		combo.setFont(FONT_BODY);
		combo.setBackground(WHITE);
		combo.setForeground(TEXT_DARK);
	}

	/** A sidebar menu button: navy, left-aligned, highlights on hover and when selected. */
	public static void styleNavButton(final JButton button) {
		flatten(button);
		button.setBackground(NAVY);
		button.setForeground(WHITE);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorder(BorderFactory.createEmptyBorder(13, 28, 13, 20));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isNavSelected(button)) {
					button.setBackground(NAVY_LIGHT);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(isNavSelected(button) ? PRIMARY : NAVY);
			}
		});
	}

	/** Marks a sidebar button as the current page (blue) or not (navy). */
	public static void setNavSelected(JButton button, boolean selected) {
		button.putClientProperty("navSelected", Boolean.valueOf(selected));
		button.setBackground(selected ? PRIMARY : NAVY);
	}

	private static boolean isNavSelected(JButton button) {
		return Boolean.TRUE.equals(button.getClientProperty("navSelected"));
	}

	/** Navy header, taller rows and a light-blue selection for every JTable. */
	public static void styleTable(JTable table) {
		table.setFont(FONT_BODY);
		table.setForeground(TEXT_DARK);
		table.setRowHeight(30);
		table.setGridColor(BORDER_GRAY);
		table.setShowVerticalLines(false);
		table.setSelectionBackground(new Color(0xDB, 0xEA, 0xFE));
		table.setSelectionForeground(TEXT_DARK);
		JTableHeader header = table.getTableHeader();
		header.setFont(FONT_LABEL);
		header.setBackground(NAVY);
		header.setForeground(WHITE);
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(0, 34));
	}

	/** Card color for a computer: green, red, orange (maintenance) or gray. */
	public static Color colorFor(AvailabilityStatus status) {
		switch (status) {
			case AVAILABLE:
				return AVAILABLE_GREEN;
			case RESERVED:
				return RESERVED_RED;
			case MAINTENANCE:
				return MAINTENANCE_ORANGE;
			default:
				return UNAVAILABLE_GRAY;
		}
	}

	/** Text color that stays readable on the card color above. */
	public static Color textColorFor(AvailabilityStatus status) {
		if (status == AvailabilityStatus.UNAVAILABLE || status == AvailabilityStatus.CLOSED) {
			return TEXT_DARK;
		}
		return WHITE;
	}

	// Shared button setup. BasicButtonUI makes the background color show on every operating system.
	private static void flatten(JButton button) {
		button.setUI(new BasicButtonUI());
		button.setOpaque(true);
		button.setFocusPainted(false);
		button.setFont(FONT_BUTTON);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
}