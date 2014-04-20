package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.CsvParser;
import model.IDataStrategy;
import model.IParser;
import model.Item;

/**
 * @author MPI
 * @version 21.04.2014/1.2
 */
public class Downloader implements Runnable {

	public static final String BCPP_REMOTE_URL = "http://euinvest.cz/generate/bcpp_data.csv";
	public static final String BCPP_LOCAL_PATH = "bcpp_data.csv";

	private IDataStrategy ds;
	private IParser ps;
	private String remoteFile;
	private String localFile;

	public Downloader(IDataStrategy ds, IParser ps, String remoteFile, String localFile) {
		this.ds = ds;
		this.ps = ps;
		this.remoteFile = remoteFile;
		this.localFile = localFile;
	}

	@Override
	public void run() {
		try {
			// download from net and save to local file
			download();
			// get max date in db
			Date lastDate = ds.getSpadLastDate();
			// parse downloaded file, get items with newer date than max date
			ArrayList<Item> r = ps.parseSpad(localFile, lastDate);
			// save parsed items to db
			ds.insertItems(r);
			//System.out.println("DW success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void download() throws IOException {
		URL url = null;
		URLConnection con = null;
		int i;

		url = new URL(remoteFile);
		con = url.openConnection();
		File file = new File(localFile);
		
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
