package view;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.Item;
import model.SpadItem;

/**
 * @author MPI
 * @version 04.05.2014/1.1
 */
public class SpadTableModel extends AbstractTableModel {
	public static final int SPAD_COLUMN_NAME = 0;
	public static final int SPAD_COLUMN_AVPRICE = 1;
	public static final int SPAD_COLUMN_AVVOLUME = 2;
	public static final int SPAD_COLUMN_DPRICEMIN = 3;
	public static final int SPAD_COLUMN_DPRICEMAX = 4;
	public static final int SPAD_COLUMN_DPRICEAVG = 5;

	protected String[] columnNames;
	protected ArrayList<SpadItem> data;

	public SpadTableModel(String[] columnNames, ArrayList<SpadItem> data) {
		super();
		this.columnNames = columnNames;
		this.data = data;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Class getColumnClass(int column) {
		switch (column) {
		case SPAD_COLUMN_NAME:
			return String.class;
		case SPAD_COLUMN_AVPRICE:
		case SPAD_COLUMN_DPRICEMIN:
		case SPAD_COLUMN_DPRICEMAX:
		case SPAD_COLUMN_DPRICEAVG:
			return Double.class;
		case SPAD_COLUMN_AVVOLUME:
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
		case SPAD_COLUMN_NAME:
			return data.get(row).getCompanyId();
		case SPAD_COLUMN_AVPRICE:
			return data.get(row).getAvPrice();
		case SPAD_COLUMN_AVVOLUME:
			return data.get(row).getAvVolume();
		case SPAD_COLUMN_DPRICEMIN:
			return data.get(row).getdPriceMin();
		case SPAD_COLUMN_DPRICEMAX:
			return data.get(row).getdPriceMax();
		case SPAD_COLUMN_DPRICEAVG:
			return data.get(row).getdPriceAvg();
		default:
			return new Object();
		}
	}
}