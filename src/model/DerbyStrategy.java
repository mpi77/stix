package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author MPI
 * @version 21.04.2014/1.1
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
	public ArrayList<Item> getSpadItems(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertItems(ArrayList<Item> items) {
		for (int i = 0; i < items.size(); i++) {
			String isql = String
					.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)",
							DerbyDatabase.DB_MAP_SPAD_TABLE, Item.DB_MAP_NAME,
							Item.DB_MAP_DATE, Item.DB_MAP_OPEN,
							Item.DB_MAP_CLOSE, Item.DB_MAP_MAX,
							Item.DB_MAP_MIN, Item.DB_MAP_VOLUME);
			try (PreparedStatement ins = db.getConnection().prepareStatement(
					isql, Statement.NO_GENERATED_KEYS)) {
				ins.setString(1, items.get(i).getName());
				ins.setDate(2, items.get(i).getDate());
				ins.setDouble(3, items.get(i).getOpen());
				ins.setDouble(4, items.get(i).getClose());
				ins.setDouble(5, items.get(i).getMax());
				ins.setDouble(6, items.get(i).getMin());
				ins.setLong(7, items.get(i).getVolume());
				Integer ia = db.actionQuery(ins, false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
