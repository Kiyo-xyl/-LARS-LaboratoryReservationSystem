package lars.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** Colors a status word in a JTable cell (Upcoming, Cancelled, Available, Maintenance ...). */
public class StatusCellRenderer extends DefaultTableCellRenderer {
	public StatusCellRenderer() {
	}

	private static final long serialVersionUID = 1L;

	private static final Color DARK_GREEN = new Color(0x16, 0xA3, 0x4A);
	private static final Color DARK_ORANGE = new Color(0xD9, 0x77, 0x06);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (!isSelected) { // keep the normal selection colors for the selected row
			cell.setForeground(colorFor(String.valueOf(value)));
		}
		return cell;
	}

	private Color colorFor(String text) {
		switch (text) {
			case "Upcoming":
				return Theme.PRIMARY;
			case "Completed":
			case "Unavailable":
				return Theme.TEXT_MUTED;
			case "Cancelled":
			case "Reserved":
				return Theme.RESERVED_RED;
			case "Available":
				return DARK_GREEN;
			case "Maintenance":
				return DARK_ORANGE;
			default:
				return Theme.TEXT_DARK;
		}
	}
}