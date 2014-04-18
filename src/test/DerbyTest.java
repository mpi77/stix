package test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Database;
import model.Item;

/**
 * @author MPI
 * @version 18.04.2014/1.1
 */
public class DerbyTest {

	public static void main(String[] args) {
		try {
			Database db = new Database();
			// insert
			String isql = String
					.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)",
							Database.DB_MAP_SPAD_TABLE, Item.DB_MAP_NAME,
							Item.DB_MAP_DATE, Item.DB_MAP_OPEN,
							Item.DB_MAP_CLOSE, Item.DB_MAP_MAX,
							Item.DB_MAP_MIN, Item.DB_MAP_VOLUME);
			try (PreparedStatement ins = db.getConnection().prepareStatement(
					isql, Statement.RETURN_GENERATED_KEYS)) {
				ins.setString(1, "CEZ");
				ins.setDate(2, new Date(System.currentTimeMillis()));
				ins.setDouble(3, 125.6);
				ins.setDouble(4, 232.2);
				ins.setDouble(5, 554.2);
				ins.setDouble(6, 21.0);
				ins.setLong(7, 156662);
				Integer ia = db.actionQuery(ins, true);
				System.out.println("Inserted row id: " + ia);
			}
			// update
			String usql = String.format("UPDATE %s SET %s=? WHERE %s=?",
					Database.DB_MAP_SPAD_TABLE, Item.DB_MAP_DATE,
					Item.DB_MAP_ID);
			try (PreparedStatement upd = db.getConnection().prepareStatement(
					usql)) {
				upd.setDate(1, new Date(System.currentTimeMillis()));
				upd.setInt(2, 1);
				Integer ua = db.actionQuery(upd, false);
				System.out.println("Updated row id: " + 1);
			}
			// select
			String ssql = String.format("SELECT * FROM %s",
					Database.DB_MAP_SPAD_TABLE);
			try (PreparedStatement ins = db.getConnection().prepareStatement(
					ssql)) {
				ArrayList<Item> a = new ArrayList<Item>();
				try (ResultSet rs = db.selectQuery(ins)) {
					while (rs.next()) {
						a.add(new Item(rs.getInt(1), rs.getString(2), rs
								.getDate(3), rs.getDouble(4), rs.getDouble(5),
								rs.getDouble(6), rs.getDouble(7), rs.getLong(8)));
						System.out.println(a.get(a.size() - 1));
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
