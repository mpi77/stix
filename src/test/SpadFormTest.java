package test;

import java.sql.SQLException;
import java.util.ArrayList;

import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.Item;
import view.SpadForm;
import view.SpadTableModel;

/**
 * @author MPI
 * @version 21.04.2014/1.0
 */
public class SpadFormTest {

	public static void main(String[] args) {
		IDataStrategy ds;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			ArrayList<Item> data = ds.getSpadItems(null, null);
			SpadTableModel model = new SpadTableModel(SpadForm.columnNames, data);
			SpadForm form = new SpadForm(model);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
