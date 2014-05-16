package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.Item;
import model.SpadItem;
import view.SpadForm;
import view.SpadTableModel;

/**
 * @author MPI
 * @version 16.05.2014/1.3
 */
public class SpadFormTest {

	public static void main(String[] args) {
		IDataStrategy ds;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			ArrayList<SpadItem> data = ds.getSpadItems(null, null);
			SpadTableModel model = new SpadTableModel(SpadForm.columnNames, data);
			SpadForm form = new SpadForm(model);
			String[] recom = ds.getPurchaseRecommendation();
			System.out.println(Arrays.deepToString(recom));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
