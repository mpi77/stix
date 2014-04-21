package view;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.Item;

/**
 * @author MPI
 * @version 21.04.2014/1.0
 */
public class SpadTableModel extends AbstractTableModel {
	public static final int SPAD_COLUMN_ID = 0;
	public static final int SPAD_COLUMN_NAME = 1;
	public static final int SPAD_COLUMN_DATE = 2;
	public static final int SPAD_COLUMN_OPEN = 3;
	public static final int SPAD_COLUMN_CLOSE = 4;
	public static final int SPAD_COLUMN_MAX = 5;
	public static final int SPAD_COLUMN_MIN = 6;
	public static final int SPAD_COLUMN_VOLUME = 7;

	protected String[] columnNames;
	protected ArrayList<Item> data;

	public SpadTableModel(String[] columnNames, ArrayList<Item> data) {
		super();
		this.columnNames = columnNames;
		this.data = data;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Class getColumnClass(int column) {
		switch (column) {
		case SPAD_COLUMN_ID:
			return Integer.class;
		case SPAD_COLUMN_NAME:
			return String.class;
		case SPAD_COLUMN_DATE:
			return Date.class;
		case SPAD_COLUMN_OPEN:
		case SPAD_COLUMN_CLOSE:
		case SPAD_COLUMN_MAX:
		case SPAD_COLUMN_MIN:
			return Double.class;
		case SPAD_COLUMN_VOLUME:
			return Long.class;
		default:
			return Object.class;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
		case SPAD_COLUMN_ID:
			return data.get(row).getId();
		case SPAD_COLUMN_NAME:
			return data.get(row).getName();
		case SPAD_COLUMN_DATE:
			return data.get(row).getDate();
		case SPAD_COLUMN_OPEN:
			return data.get(row).getOpen();
		case SPAD_COLUMN_CLOSE:
			return data.get(row).getClose();
		case SPAD_COLUMN_MAX:
			return data.get(row).getMax();
		case SPAD_COLUMN_MIN:
			return data.get(row).getMin();
		case SPAD_COLUMN_VOLUME:
			return data.get(row).getVolume();
		default:
			return new Object();
		}
	}
}
