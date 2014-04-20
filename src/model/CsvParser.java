package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CsvParser implements IParser {

	@Override
	public ArrayList<Item> parseSPAD(String filename, Date minimalDate)
			throws IOException {
		ArrayList<Item> r = null;
		File f = new File(filename);
		if (!f.isFile()) {
			throw new FileNotFoundException();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			r = new ArrayList<Item>();
			System.out.println(minimalDate.toString());
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(",");
				java.sql.Date itemDate;
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					java.util.Date parsed = (java.util.Date) format
							.parse(cols[1]);
					itemDate = new java.sql.Date(parsed.getTime());
				} catch (ParseException e) {
					itemDate = new Date(0);
				}

				if (minimalDate != null && itemDate.before(minimalDate)) {
					continue;
				}

				r.add(new Item(null,
						cols[0].substring(1, cols[0].length() - 1), itemDate,
						Double.valueOf(cols[2]), Double.valueOf(cols[3]),
						Double.valueOf(cols[4]), Double.valueOf(cols[5]), Long
								.valueOf(cols[6])));
			}
		}
		return r;
	}
}
