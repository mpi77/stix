package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class CsvParser implements IParser {

	@Override
	public ArrayList<Item> parseSPAD(String filename, Date minimalDate) throws IOException {
		ArrayList<Item> r = null;
		File f = new File(filename);
		if (!f.isFile()) {
			throw new FileNotFoundException();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){
			String line; 
			r = new ArrayList<Item>();
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(",");
				// TODO: if(minimalDate != null && minimalDate < cols[1]) break;
				r.add(new Item(null, cols[0].substring(1, cols[0].length() - 1), 
						new Date(System.currentTimeMillis()), 
						Double.valueOf(cols[2]), Double.valueOf(cols[3]), 
						Double.valueOf(cols[4]), Double.valueOf(cols[5]), 
						Long.valueOf(cols[6])));
			}
		}
		return r;
	}
}
