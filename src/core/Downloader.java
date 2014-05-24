package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import view.MainGui;
import model.IDataStrategy;
import model.IParser;
import model.Item;

/**
 * @author MPI
 * @version 24.05.2014/1.4
 */
public class Downloader implements Runnable {

	public static final String BCPP_REMOTE_URL = "http://euinvest.cz/generate/bcpp_data.csv";
	public static final String BCPP_LOCAL_PATH = "bcpp_data.csv";

	private IDataStrategy ds;
	private IParser ps;
	private String remoteFile;
	private String localFile;
	private String workingDir = System.getProperty("user.dir");
	private MainGui gui;

	public Downloader(IDataStrategy ds, IParser ps, String remoteFile,
			String localFile, MainGui gui) {
		this.ds = ds;
		this.ps = ps;
		this.remoteFile = remoteFile;
		this.localFile = localFile;
		this.gui = gui;
	}

	@Override
	public void run() {
		try {
			// download from net and save to local file
			download();
			// get max date in db
			Date lastDate = ds.getSpadLastDate();
			if (lastDate != null) {
				// need to increment date
				Calendar cal = Calendar.getInstance();
				cal.setTime(lastDate);
				cal.add(Calendar.DATE, 1);
				lastDate = new Date(cal.getTimeInMillis());
			}
			// parse downloaded file, get items with newer date than max date
			ArrayList<Item> r = ps.parseSpad(
					String.format("%s/%s", workingDir, localFile), lastDate);
			// save parsed items to db
			ds.insertItems(r);
			// System.out.println("DW saved " + r.size() + " new items");
			if (gui == null) {
				System.out.println("Downloader finished.");
			} else {
				gui.setStatusLabel("Downloader finished.");
			}
		} catch (IOException | SQLException e) {
			if (gui == null) {
				System.out.println(e.getStackTrace());
			} else {
				gui.setStatusLabel("Downloader error.");
			}
		}
	}

	private void download() throws IOException {
		URL url = null;
		URLConnection con = null;
		int i;

		url = new URL(remoteFile);
		con = url.openConnection();
		File file = new File(String.format("%s/%s", workingDir, localFile));

		try (BufferedInputStream bis = new BufferedInputStream(
				con.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file.getName()));) {
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
			bos.flush();
		}
	}
}
