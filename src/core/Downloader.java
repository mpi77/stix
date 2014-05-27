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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import view.MainGui;
import model.IDataStrategy;
import model.IParser;
import model.Item;

/**
 * @author MPI
 * @version 27.05.2014/1.9
 */
public class Downloader implements Runnable {

	public static final String BCPP_REMOTE_URL = "http://euinvest.cz/generate/bcpp_data.csv";
	public static final String BCPP_LOCAL_PATH = "bcpp_data.csv";
	public static final int STATUS_SUCCES = 1;
	public static final int STATUS_FAIL = 2;
	
	private static Logger logger = Logger.getLogger("MainDriverLooger");
	
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
			updateLastDate();
			updateTable();
			gui.checkDownloader(STATUS_SUCCES);
			reportProgress("Downloader finished.");
		} catch (Exception e) {
			logger.error("DW:Exception occured:", e);
			gui.checkDownloader(STATUS_FAIL);
			reportProgress("Downloader error.");
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

	private void reportProgress(final String msg) {
		if (gui == null) {
			System.out.println(msg);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					gui.setStatusLabel(msg);
				}
			});
		}
	}

	private void updateLastDate() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					gui.setLastDateLabel(new SimpleDateFormat("dd.MM.yyyy")
							.format(ds.getSpadLastDate()));
				} catch (NullPointerException | IllegalArgumentException
						| SQLException e) {
					reportProgress("Downloader error.");
				}
			}
		});
	}

	private void updateTable() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					gui.setDefaultFilter();
					gui.refreshTable();
				} catch (SQLException e) {
					reportProgress("Downloader error.");
				}
			}
		});
	}
}
