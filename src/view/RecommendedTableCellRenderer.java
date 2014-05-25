package view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author MPI
 * @version 25.05.2014/1.0
 */
public class RecommendedTableCellRenderer extends DefaultTableCellRenderer {
	ArrayList<Integer> selectedRows;

	public RecommendedTableCellRenderer(ArrayList<Integer> selectedRows) {
		this.selectedRows = selectedRows;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean selected, boolean focused, int row, int column) {
		setEnabled(table == null || table.isEnabled());

		if (selectedRows != null) {
			for (int i = 0; i < selectedRows.size(); i++) {
				if (row == selectedRows.get(i)) {
					setBackground(Color.green);
				} else {
					setBackground(Color.white);
				}
			}
		}

		super.getTableCellRendererComponent(table, value, selected, focused,
				row, column);
		return this;
	}
}
