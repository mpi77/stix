package core;

import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import view.MainGui;

/**
 * @author MPI
 * @version 23.05.2014/1.1
 */
public class MainDriver {

	public static void main(String[] args) {
		IDataStrategy ds;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			MainGui window = new MainGui(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
