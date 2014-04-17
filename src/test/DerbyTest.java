package test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Database;
import model.Item;

public class DerbyTest {

	public static void main(String[] args) {
		try {
			Database db = new Database();
			String isql = String
					.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?)",
							Database.DB_MAP_SPAD_TABLE, Item.DB_MAP_NAME,
							Item.DB_MAP_DATE, Item.DB_MAP_OPEN,
							Item.DB_MAP_CLOSE, Item.DB_MAP_MAX,
							Item.DB_MAP_MIN, Item.DB_MAP_VOLUME);
			try (PreparedStatement ins = db.getConnection().prepareStatement(
					isql, Statement.RETURN_GENERATED_KEYS)) {
				ins.setString(1, "CEZ");
				ins.setString(2,
						new Date(System.currentTimeMillis()).toString());
				ins.setDouble(3, 125.6);
				ins.setDouble(4, 232.2);
				ins.setDouble(5, 554.2);
				ins.setDouble(6, 21.0);
				ins.setInt(7, 156662);
				int ia = db.actionQuery(ins, true);
				System.out.println(ia);
			}
			try (PreparedStatement ins = db.getConnection().prepareStatement("SELECT * FROM SPAD")) {
				try(ResultSet rs = db.selectQuery(ins)){
					while(rs.next()){
						System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getDate(3));
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
