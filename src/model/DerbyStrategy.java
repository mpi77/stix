package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import view.SpadTableModel;

/**
 * @author MPI
 * @version 04.05.2014/1.4
 */
public class DerbyStrategy implements IDataStrategy {

	private DerbyDatabase db;

	public DerbyStrategy(DerbyDatabase db) {
		this.db = db;
	}

	@Override
	public Date getSpadLastDate() {
		Date lastDate = null;
		String ssql = String.format("SELECT MAX(%s) FROM %s", Item.DB_MAP_DATE,
				DerbyDatabase.DB_MAP_SPAD_TABLE);
		try (PreparedStatement ins = db.getConnection().prepareStatement(ssql)) {
			try (ResultSet rs = db.selectQuery(ins)) {
				if (rs.next()) {
					lastDate = rs.getDate(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastDate;
	}

	@Override
	public ArrayList<Item> getItems(Date startDate, Date endDate) {
		ArrayList<Item> r = new ArrayList<Item>();
		// TODO: startDate, endDate conditions
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	@Override
	public void insertItem(Item item) {
		String isql = String
				.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)",
						DerbyDatabase.DB_MAP_SPAD_TABLE, Item.DB_MAP_NAME,
						Item.DB_MAP_DATE, Item.DB_MAP_OPEN,
						Item.DB_MAP_CLOSE, Item.DB_MAP_MAX,
						Item.DB_MAP_MIN, Item.DB_MAP_VOLUME);
		try (PreparedStatement ins = db.getConnection().prepareStatement(
				isql, Statement.NO_GENERATED_KEYS)) {
			ins.setString(1, item.getName());
			ins.setDate(2, item.getDate());
			ins.setDouble(3, item.getOpen());
			ins.setDouble(4, item.getClose());
			ins.setDouble(5, item.getMax());
			ins.setDouble(6, item.getMin());
			ins.setLong(7, item.getVolume());
			Integer ia = db.actionQuery(ins, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertItems(ArrayList<Item> items) {
		for (int i = 0; i < items.size(); i++) {
			insertItem(items.get(i));
		}
	}

	@Override
	public ArrayList<SpadItem> getSpadItems(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
