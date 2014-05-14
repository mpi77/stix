package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 14.05.2014/1.8
 */
public class DerbyStrategy implements IDataStrategy {

	private DerbyDatabase db;

	public DerbyStrategy(DerbyDatabase db) {
		this.db = db;
	}

	@Override
	public Date getSpadFirstDate() throws SQLException {
		Date firstDate = null;
		String ssql = String.format("SELECT MIN(%s) FROM %s", Item.DB_MAP_DATE,
				DerbyDatabase.DB_MAP_SPAD_TABLE);
		try (PreparedStatement ins = db.getConnection().prepareStatement(ssql)) {
			try (ResultSet rs = db.selectQuery(ins)) {
				if (rs.next()) {
					firstDate = rs.getDate(1);
				}
			}
		}
		return firstDate;
	}

	@Override
	public Date getSpadLastDate() throws SQLException {
		Date lastDate = null;
		String ssql = String.format("SELECT MAX(%s) FROM %s", Item.DB_MAP_DATE,
				DerbyDatabase.DB_MAP_SPAD_TABLE);
		try (PreparedStatement ins = db.getConnection().prepareStatement(ssql)) {
			try (ResultSet rs = db.selectQuery(ins)) {
				if (rs.next()) {
					lastDate = rs.getDate(1);
				}
			}
		}
		return lastDate;
	}

	@Override
	public ArrayList<Item> getItems(Date startDate, Date endDate)
			throws SQLException {
		ArrayList<Item> r = new ArrayList<Item>();
		if (startDate == null) {
			startDate = this.getSpadFirstDate();
		}
		if (endDate == null) {
			endDate = this.getSpadLastDate();
		}
		String ssql = String.format("SELECT %s,%s,%s,%s,%s,%s,%s,%s FROM %s",
				Item.DB_MAP_ID, Item.DB_MAP_NAME, Item.DB_MAP_DATE,
				Item.DB_MAP_OPEN, Item.DB_MAP_CLOSE, Item.DB_MAP_MAX,
				Item.DB_MAP_MIN, Item.DB_MAP_VOLUME,
				DerbyDatabase.DB_MAP_SPAD_TABLE);
		try (PreparedStatement sel = db.getConnection().prepareStatement(ssql)) {
			try (ResultSet rs = db.selectQuery(sel)) {
				while (rs.next()) {
					Item a = new Item(rs.getInt(1), rs.getString(2),
							rs.getDate(3), rs.getDouble(4), rs.getDouble(5),
							rs.getDouble(6), rs.getDouble(7), rs.getLong(8));
					r.add(a);
				}
			}
		}
		return r;
	}

	@Override
	public void insertItem(Item item) throws SQLException {
		String isql = String.format(
				"INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)",
				DerbyDatabase.DB_MAP_SPAD_TABLE, Item.DB_MAP_NAME,
				Item.DB_MAP_DATE, Item.DB_MAP_OPEN, Item.DB_MAP_CLOSE,
				Item.DB_MAP_MAX, Item.DB_MAP_MIN, Item.DB_MAP_VOLUME);
		try (PreparedStatement ins = db.getConnection().prepareStatement(isql,
				Statement.NO_GENERATED_KEYS)) {
			ins.setString(1, item.getName());
			ins.setDate(2, item.getDate());
			ins.setDouble(3, item.getOpen());
			ins.setDouble(4, item.getClose());
			ins.setDouble(5, item.getMax());
			ins.setDouble(6, item.getMin());
			ins.setLong(7, item.getVolume());
			Integer ia = db.actionQuery(ins, false);
		}
	}

	@Override
	public void insertItems(ArrayList<Item> items) throws SQLException {
		for (int i = 0; i < items.size(); i++) {
			insertItem(items.get(i));
		}
	}

	@Override
	public ArrayList<SpadItem> getSpadItems(Date startDate, Date endDate)
			throws SQLException {
		ArrayList<SpadItem> r = new ArrayList<SpadItem>();
		if (startDate == null) {
			startDate = this.getSpadFirstDate();
		}
		if (endDate == null) {
			endDate = this.getSpadLastDate();
		}
		/*
		 * ssql structure
		 * 1 - company id
		 * 2 - avg price (open+close/2)
		 * 3 - avg volume
		 * 4 - min price
		 * 5 - max price
		 * 6 - avg price (open+close/2)
		 */
		String ssql = String
				.format("SELECT %s, AVG((%s+%s)/2), AVG(%s), MIN(%s), "
						+ "MAX(%s), AVG((%s+%s)/2) FROM %s,%s "
						+ "WHERE (%s=%s AND %s >= ? AND %s <= ?) GROUP BY %s",
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_OPEN,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_CLOSE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_VOLUME,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_CLOSE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_CLOSE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_OPEN,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_CLOSE,
						DerbyDatabase.DB_MAP_SPAD_TABLE, DerbyDatabase.DB_MAP_COMPANY_TABLE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_NAME,
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE,
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID);
		try (PreparedStatement sel = db.getConnection().prepareStatement(ssql)) {
			sel.setDate(1, startDate);
			sel.setDate(2, endDate);
			try (ResultSet rs = db.selectQuery(sel)) {
				while (rs.next()) {
					SpadItem a = new SpadItem(rs.getString(1), rs.getString(1),
							rs.getDouble(2), rs.getLong(3), rs.getDouble(4),
							rs.getDouble(5), rs.getDouble(6));
					r.add(a);
				}
			}
		}
		/*
		 * rsql structure - fetch newest data
		 * 1 - company id
		 * 2 - company name
		 * 3 - actual avg price (per day)
		 * 4 - date
		 */
		String rsql = String
				.format("SELECT %s,%s,((%s+%s)/2),%s "
						+ "FROM %s INNER JOIN %s ON %s=%s  "
						+ "WHERE (%s = ? AND %s >= ? AND %s <= ?) "
						+ "ORDER BY %s DESC FETCH FIRST 1 ROWS ONLY",
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID,
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_NAME,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_OPEN,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_CLOSE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE,
						DerbyDatabase.DB_MAP_SPAD_TABLE, DerbyDatabase.DB_MAP_COMPANY_TABLE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_NAME,
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID,
						DerbyDatabase.DB_MAP_COMPANY_TABLE+"."+Company.DB_MAP_ID,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE,
						DerbyDatabase.DB_MAP_SPAD_TABLE+"."+Item.DB_MAP_DATE);
		//System.out.println("last date in db: " + this.getSpadLastDate());
		for (int i = 0; i < r.size(); i++) {
			try (PreparedStatement sel = db.getConnection().prepareStatement(
					rsql)) {
				sel.setString(1, r.get(i).getCompanyId());
				sel.setDate(2, startDate);
				sel.setDate(3, endDate);
				try (ResultSet rs = db.selectQuery(sel)) {
					while (rs.next()) {
						// System.out.println(rs.getString(1) + " "+
						// rs.getDate(3) + " " + rs.getDouble(2));
						r.get(i).setCompanyName(rs.getString(2));
						r.get(i).setdPriceMin(
								rs.getDouble(3) - r.get(i).getdPriceMin());
						r.get(i).setdPriceMax(
								rs.getDouble(3) - r.get(i).getdPriceMax());
						r.get(i).setdPriceAvg(
								r.get(i).getdPriceAvg() - rs.getDouble(3));
					}
				}
			}
		}
		return r;
	}

}
