package test;

import java.sql.SQLException;

import model.CsvParser;
import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.IParser;
import core.Downloader;

/**
 * @author MPI
 * @version 21.04.2014/1.1
 */
public class DownloaderTest {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);
		   
		IDataStrategy ds;
		IParser ps;
		try {
			ds = new DerbyStrategy(new DerbyDatabase());
			ps = new CsvParser();
			Downloader d = new Downloader(ds, ps, Downloader.BCPP_REMOTE_URL, Downloader.BCPP_LOCAL_PATH);
			Thread td = new Thread(d);
			td.start();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
