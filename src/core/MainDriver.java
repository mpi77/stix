package core;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import view.MainGui;

/**
 * @author MPI
 * @version 27.05.2014/1.4
 */
public class MainDriver {

	private static Logger logger = Logger.getLogger("MainDriverLooger");

	public static void main(String[] args) {
		IDataStrategy ds;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			MainGui window = new MainGui(ds);
		} catch (Exception e) {
			logger.error("MD:Exception occured:", e);
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
