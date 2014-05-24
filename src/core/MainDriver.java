package core;

import javax.swing.JOptionPane;

import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import view.MainGui;

/**
 * @author MPI
 * @version 24.05.2014/1.2
 */
public class MainDriver {

	public static void main(String[] args) {
		IDataStrategy ds;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			MainGui window = new MainGui(ds);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
