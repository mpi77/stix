package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import model.Company;
import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.Item;
import model.SpadItem;
import view.SpadForm;
import view.SpadTableModel;

/**
 * @author MPI
 * @version 16.05.2014/1.4
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
			System.out.println("Recommendation: " + Arrays.deepToString(recom));
			ArrayList<Company> companies = ds.getCompanies();
			System.out.println("Companies: " + companies);
			System.out.println("Last date: " + ds.getSpadLastDate());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
